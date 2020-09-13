## NaraeImagePicker [![Build Status](https://build.uzuki.live/job/NaraeImagePicker/job/master/badge/icon)](https://build.uzuki.live/job/NaraeImagePicker/job/master/) [![](https://jitpack.io/v/WindSekirun/NaraeImagePicker.svg)](https://jitpack.io/#WindSekirun/NaraeImagePicker)

[![Kotlin](https://img.shields.io/badge/kotlin-1.3.3-blue.svg)](http://kotlinlang.org)	[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-NaraeImagePicker-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/6695)

MultiImagePicker for Android Application, written in [Kotlin](http://kotlinlang.org) 

<img src="https://github.com/WindSekirun/NaraeImagePicker/blob/master/sample.png" width="600" height="308">

It provides...
 * Pick multiple image
 * Provide two viewMode, FolderView and FileView
 * Check Runtime Permissions automatically
 * Show detail of Image
 * Support Tablet Layout

## Usages

### Import

*rootProject/build.gradle*
```
allprojects {
    repositories {
	    maven { url 'https://jitpack.io' }
    }
}
```

*app/build.gradle*
```
dependencies {
     implementation 'com.github.WindSekirun:NaraeImagePicker:2.0.6'
}
```

**Warning, I changed package name starting from 2.0.0.**

### Activity
```kotlin
val settingItem = PickerSettingItem().apply {
    pickLimit = Constants.LIMIT_UNLIMITED
    viewMode = ViewMode.FolderView
    enableDetailMode = true
    uiSetting.enableUpInParentView = true
    uiSetting.themeResId = R.style.AppTheme
    uiSetting.pickerTitle = "Custom Picker Title"
    uiSetting.fileSpanCount = 3
    uiSetting.folderSpanCount = 2
}

NaraeImagePicker.instance.start(this, settingItem, object : OnPickResultListener {
    override fun onSelect(resultCode: Int, imageList: ArrayList<String>) {
        if (resultCode != NaraeImagePicker.PICK_SUCCESS) {
            toast("Failed to pick image")
            return
        }

        
    }
})
```

### resolve DuplicateRelativeFileException: More than one file...
You should insert this statement in android block in app/build.gradle

```
packagingOptions {
    exclude 'META-INF/library_release.kotlin_module'
}
```

## Revision History
* ver 2.0.7 (2020-09-13)
  * Migrate to AndroidX, update dependencies

* ver 2.0.6 (2019-10-21)
  * Fix #20 Fix maintain state of fragment while selecting images (thanks to @deepakkumardk )

* ver 2.0.5 (2019-10-13)
  * Fix #19 Add field to include and remove gif (thanks to @deepakkumardk )

* ver 2.0.0 (2019-05-15)
  * Fix #12 Show selected image count in the FileView, FolderView (thanks to @deepakkumardk )
  * Fix #13 Change row layout to ConstraintLayout
  * Fix #15 Remaining appName when starting NaraeImagePicker
  * Change Package Name
  * New Sample Application Design
  * Add functionality to change span count in FileView, FolderView
  * Update library version to latest version

* ver 1.8.0 (2018-12-06)
  * PR #11 add UISettings
  * Issue #10 Cannot load .webp image in zoom view
  * Optimize some code follows by Codebeat
  * Remove unused reflect package to reduce method count
  * Remove 'allowBackup' statement in AndroidManifest.xml

* ver 1.7.1 (2018-12-04)
  * PR #9 Fix NaraePickerActivity.kt to unregist EventBus when lifecycle onStop. (Thanks to @zeallat )

* ver 1.7.0 (2018-12-04)
  * PR #8 Add attribute to able to configure custom theme (thanks to @zeallat)
  * Fix 'Zoom mode' is not working when set false to disableZoomMode in PickerSettingItem

For older history, refer to [RELEASE.md](RELEASE.md)

## License

```
Copyright 2017 WindSekirun (DongGil, Seo)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
