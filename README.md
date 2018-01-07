## NaraeImagePicker [![](https://jitpack.io/v/WindSekirun/NaraeImagePicker.svg)](https://jitpack.io/#WindSekirun/NaraeImagePicker) [![CircleCI](https://circleci.com/gh/WindSekirun/NaraeImagePicker.svg?style=svg)](https://circleci.com/gh/WindSekirun/NaraeImagePicker)

[![Kotlin](https://img.shields.io/badge/kotlin-1.2.0-blue.svg)](http://kotlinlang.org)	[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

MultiImagePicker for Android Application, written in [Kotlin](http://kotlinlang.org)

<img src="https://github.com/WindSekirun/NaraeImagePicker/blob/master/sample.png" width="600" height="308">

It provides...
* Pick multi image
* Provide two mode of activity, One is Folder-View, other is File-View.
* Support show detail of Image (using PhotoView)
* check Runtime Permissions automatically

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
     implementation 'com.github.WindSekirun:NaraeImagePicker:1.0.1'
}
```

#### Code
```
NaraeImagePicker.instance.start(this, Constants.LIMIT_UNLIMITED, new OnPickResultListener() {
            @Override
            public void onSelect(int resultCode, @NotNull ArrayList<String> imageList) {
               
            }
});
```

No need to implement ```onActivityResult```, NaraeImagePicker will handle ```startActivityForResult```, ```onActivityResult``` for you. 

* Starting folder-view activity: NaraeImagePicker.start(Context, int, OnPickResultListener)
* Starting file-view activity: NaraeImagePicker.startAll(Context, int, OnPickResultListener)

#### Customize
I don't have plan of providing customize, but you can customize few options by this methods.

##### Title of toolbar
* Extends ```narae_image_picker_album_title``` resources in Strings.xml

##### Error message when limit exceeded
* Extends ```narae_image_picker_limit_exceed``` resources in Strings.xml

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
