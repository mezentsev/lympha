# Lympha
Lympha is a library for logging by annotation-triggered method call.

## How to use
For example:
```kotlin
@LymphaProfile
infix fun <T> Collection<T>.areSameContentWith(collection: Collection<T>?)
            = collection?.let { this.size == it.size && this.containsAll(it) }
```

Output will be:
```
[Thread(main)] Obtained event message: '⇢ areSameContentWith(interface java.util.Collection $this$areSameContentWith = "[100]", interface java.util.Collection collection = "[200]")' and took '0 ms'
[Thread(main)] Obtained event message: '⇠ areSameContentWith = Boolean "false"' and took '0 ms'
```

Example application can be found [here](https://github.com/mezentsev/lympha/tree/master/lympha-test-app).

## Getting started

The first step is to include Lympha Profiler into your project and apply aspectj plugin:

```groovy
...

apply plugin: 'com.archinamon.aspectj'

aspectj {
    includeAspectsFromJar 'lympha-profiler'
}

...

dependencies {
    debugImplementation 'pro.mezentsev.lympha:lympha-profiler:0.1.0'
    releaseImplementation 'pro.mezentsev.lympha:lympha-profiler-noop:0.1.0'
}
```
`lympha-profiler-noop` doesn't contain any aspectj, hence it can be used in release implementations.

Also you must add aspecj to classpath:
```groovy
buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        ...
        classpath "com.archinamon:android-gradle-aspectj:3.4.0"
    }
}
```

Initialize Lympha in Application:
```kotlin
class LymphaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Lympha.init()
    }
}
```
You can customize it with custom Logger using ```Lympha.Builder```

Add ```@LymphaProfiler``` to your methods:
```kotlin
@LymphaProfiler
private fun obtainNumber(number: Int): Int {
    Thread.sleep(50)
    return number * number
}
```
and you will see on every call:
```
[Thread(main)] Obtained event message: '⇢ obtainNumber(int number = "5")' and took '0 ms'
[Thread(main)] Obtained event message: '⇠ obtainNumber = Integer "25"' and took '52 ms'
```

## Subscribing to events

You can subscribe to every event caught by Lympha. It could be reached by adding listener:
```kotlin
Lympha.addEventListener(object : EventListener {
    override fun eventObtained(event: Event) {
       // operate with event
    }
})
```

Event has simple structure:
- ```message: String```
- ```timeTaken: Long```
- ```timestamp: Long```
- ```threadName: String```

## Compatibility
* **Minimum Android SDK**: requires a minimum API level of 16.
* **Compile Android SDK**: requires you to compile against API 29.

## Built With
* [AspectJ](https://github.com/Archinamon/android-gradle-aspectj) - AspectJ Gradle Plugin

## Authors
Vadim Mezentsev
