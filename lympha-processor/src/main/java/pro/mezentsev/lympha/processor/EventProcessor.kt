package pro.mezentsev.lympha.processor

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType
import pro.mezentsev.lympha.annotation.EventComposer
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.tools.Diagnostic

// registering service
@AutoService(Processor::class)
@SupportedOptions(EventProcessor.KOTLIN_GEN_DIRECTORY)
@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.ISOLATING)
class EventProcessor : AbstractProcessor() {

    companion object {
        /** Kotlin generated code target directory option name. */
        const val KOTLIN_GEN_DIRECTORY = "kapt.kotlin.generated"
    }

    private val annotation = EventComposer::class.java

    /**
     * Kotlin generated code target directory.
     **/
    private val targetDirectory: String
        get() = processingEnv.options[KOTLIN_GEN_DIRECTORY]
            ?: throw IllegalStateException("Unable to get target directory")

    /**
     * Provides set of annotations [EventProcessor] can make use of.
     **/
    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(annotation.canonicalName)
    }

    /**
     * Provides supported version of Java.
     **/
    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    /**
     * Method should return true to indicate that the annotations have been claimed by this processor.
     */
    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        if (annotations.isEmpty()) {
            return false
        }

        //  generate only once, subsequent runs provide empty set
        if (roundEnv.processingOver()) {
            return true
        }

        roundEnv
            .getElementsAnnotatedWith(annotation)
            .filter { it.kind == ElementKind.METHOD }
            .forEach { annotatedElement ->
                processingEnv.messager.printMessage(Diagnostic.Kind.NOTE,
                    annotatedElement.enclosingElement.simpleName
                );
                val pack = processingEnv.elementUtils.getPackageOf(annotatedElement).qualifiedName.toString()
                val annotatedClassName = annotatedElement.simpleName.toString()
                val parameters = (annotatedElement as ExecutableElement).parameters
                generateTimingLogger(pack, annotatedClassName, parameters)
            }

        return true
    }

    private fun generateTimingLogger(
        pack: String,
        annotatedName: String,
        parameters: List<VariableElement>
    ) {
        val fileName = "${annotatedName}Profiler"
        val informParameters = parameters.joinToString { "${it.simpleName}: ${it.asType()}" }

        val informerClass = ClassName("pro.mezentsev.lympha.events", "Informer")
        val eventClass = ClassName("pro.mezentsev.lympha.events", "Event")
        val callerMethod = FunSpec
            .builder("start")
            .addStatement("%T.inform(%T(%T::class.java, \"$informParameters\"))", informerClass, eventClass, eventClass)
            .build()
        val generatedClass = TypeSpec
            .objectBuilder(fileName)
            .addFunction(callerMethod)
            .build()
        val generatedFile = FileSpec
            .builder(pack, fileName)
            .addType(generatedClass)
            .build()

        generatedFile.writeTo(processingEnv.filer)
        //generatedFile.writeTo(File(targetDirectory, "$fileName.kt"))
    }
}