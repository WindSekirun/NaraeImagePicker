## NaraeImagePicker [![](https://jitpack.io/v/WindSekirun/NaraeImagePicker.svg)](https://jitpack.io/#WindSekirun/NaraeImagePicker) [![CircleCI](https://circleci.com/gh/WindSekirun/NaraeImagePicker.svg?style=svg)](https://circleci.com/gh/WindSekirun/NaraeImagePicker) <a href="https://codebeat.co/projects/github-com-windsekirun-naraeimagepicker-master"><img alt="codebeat badge" src="https://codebeat.co/badges/d974046a-c9a9-40f8-95f6-70ffbf77a3ee" /></a>

[![Kotlin](https://img.shields.io/badge/kotlin-1.2.0-blue.svg)](http://kotlinlang.org)	[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0) [![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-NaraeImagePicker-green.svg?style=flat )]( https://android-arsenal.com/details/1/6695 ) 

MultiImagePicker for Android Application, written in [Kotlin](http://kotlinlang.org)

<img src="https://github.com/WindSekirun/NaraeImagePicker/blob/master/sample.png" width="600" height="308">

It provides...
* Pick multi image
* Provide two mode of activity, One is Folder-View, other is File-View.
* Support show detail of Image (using PhotoView) - support .gif, .webp
* Check 'Runtime Permissions' automatically

### Usages
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
     implementation 'com.github.WindSekirun:NaraeImagePicker:1.7.1'
}
```

#### Code
```Java
PickerSettingItem item = new PickerSettingItem();
item.setPickLimit(Constants.LIMIT_UNLIMITED);
item.setViewMode(ViewMode.FolderView);

NaraeImagePicker.instance.start(this, item, new OnPickResultListener() {
    @Override
    public void onSelect(int resultCode, @NotNull ArrayList<String> imageList) {
        if (resultCode == NaraeImagePicker.PICK_SUCCESS) {
            itemList.addAll(imageList);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(MainActivity.this, "failed to pick image", Toast.LENGTH_SHORT).show();
        }
    }
});
```

No need to implement ```onActivityResult```, NaraeImagePicker will handle ```startActivityForResult```, ```onActivityResult``` for you. 

## Customization in PickerSettingItem

```kotlin
    var viewMode = ViewMode.FolderView
    var pickLimit = Constants.LIMIT_UNLIMITED
    var disableZoomMode = false
    var uiSetting: UISetting = UISetting()

    class UISetting {
        var themeResId: Int? = null
        var pickerTitle = "Please select picture."
        var exceedLimitMessage = "Can\\'t select more than %s pictures"
        var enableUpInParentView = false
    }
```

## resolve DuplicateRelativeFileException: More than one file...
you should insert this statement in android block in app/build.gradle

```
packagingOptions {
    exclude 'META-INF/library_release.kotlin_module'
}
```

## Revision History
* ver 1.8.0 (2018-12-??) **NOT RELEASED**
  * PR #11 add UISettings
  * Issue #10 Cannot load .webp image in zoom view
  * Optimize some code follows by Codebeat
  * Remove unused reflect package to reduce method count
  * Remove 'allowBackup' statement in AndroidManifest.xml

* ver 1.7.1 (2018-12-04)
  * PR #9 Fix NaraePickerActivity.kt to unregist EventBus when lifecycle onStop. (Thanks to @zeallat )

* ver 1.7.0 (2018-12-04)
  * PR #8 Add attribute to able to configure custom theme (thanks to @zeallat )
  * Fix 'Zoom mode' is not working when set false to disableZoomMode in PickerSettingItem

* ver 1.6.2 (2018-04-23)
  * revert 64162d0 and insert getFolderList

* ver 1.6.1 (2018-04-23)
  * try-catch in getFolderList

* ver 1.6.0 (2018-03-27)
  * add disableZoomMode in PickerSettingItem

* ver 1.5.0 (2018-02-27)
  * Implement PickerSet instead loading content on Fragment.

* ver 1.1.1 (2018-02-12)
  * Issue #4 Native Fragment will deprecated in Android P

* ver 1.1.0 (2018-01-12)
  * downscaling AlbumAdapter, ImageAdapter

* ver 1.0.1 (2018-01-11)
  * insert default theme

* ver 1.0.0 (2018-01-06)
  * First release of NaraeImagePicker.

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
