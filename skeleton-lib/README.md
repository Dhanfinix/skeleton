# Skeleton AAR Wrapper (`:skeleton-lib`)

This module serves as a wrapper for the local `skeleton.aar` file. It exists solely to resolve build errors related to direct local AAR dependencies in the Android Gradle Plugin (AGP).

## ⚠️ The Problem

When attempting to build the signed APK or compile the `:core` library, the build fails with the following error:

> Direct local .aar file dependencies are not supported when building an AAR. The resulting AAR would be broken because the classes and Android resources from any local .aar file dependencies would not be packaged in the resulting AAR.
>
> Previous versions of the Android Gradle Plugin produce broken AARs in this case too (despite not throwing this error). The following direct local .aar file dependencies of the :core project caused this error: ...

### Root Cause

This error occurs because our feature modules (like `home`, `leave`, etc.) and the `:core` library are compiled into AARs.

Previously, we were importing the dependency directly using a file path:

```groovy
// ❌ Old Approach (Causes Build Failure)
implementation files("../app/libs/skeleton.aar")
```

The Android build system **cannot** package a local `.aar` inside another `.aar`. It does not know how to correctly merge the inner AAR's resources (layouts, manifests, R.class) into the parent library (`core.aar`).

## ✅ The Solution

To fix this, we isolated `skeleton.aar` into this dedicated Gradle module (`:skeleton-lib`).

Instead of consuming a raw file, our libraries now consume this module as a project dependency. This allows Gradle to link the resources correctly during the app build process without trying to embed one AAR inside another.

**Usage in `shared_dependencies.gradle` or any build.gradle:**

```groovy
// ✅ Correct Approach
implementation project(':skeleton-lib')
```