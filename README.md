# KeyboardHeaderLayout
[![](https://jitpack.io/v/ZeyuKeithFu/KeyboardHeaderLayout.svg)](https://jitpack.io/#ZeyuKeithFu/KeyboardHeaderLayout)

`KeyboardHeaderLayout` adds a sticky header above the keyboard. You can customize the UI (maybe adding an emoji selection tab or a line of hashtags) without concerning about the keyboard state.

## Integartion
Add the JitPack repository in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Add the dependency
```
	dependencies {
	        implementation 'com.github.ZeyuKeithFu:KeyboardHeaderLayout:v1.0'
	}
```

## Usage
A `ConstraintLayout` with a little more functionality. Take a look at the `demo` directory.

## Reference
This project is highly inspired by [Siebe](https://github.com/siebeprojects/samples-keyboardheight), and some code is originally from his project. Big shout out.
