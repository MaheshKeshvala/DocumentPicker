# Document Picker Android

Document Picker is the Android single or multiple file picker library use for the file selection from the SD card or Internal storage.

## Library Setup

Use the below gradle dependency for the implementation of the document picker library.

Add it in your root build.gradle at the end of repositories:
```bash
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Add the dependency into you app level gradle file(build.gradle):

	dependencies {
	        implementation 'com.github.MaheshKeshvala:DocumentPicker:6311127383'
	}

## Usage

Add Below code where you want to open the Document picker
```
// Request code is the code used for the onActivity result handle and false is mention for the multiple file selection.
// if you want multiple files then pass true into it.
 DocumentPickerClass.browseDocuments(this, RequestCode,false);
```
Put below code into your onActivity result override method to handle the library result:
```
   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fileList = DocumentPickerClass.onActivityResult(this, requestCode, resultCode, data);
    }
```

Put the below code to handle the permission which required for latest android version
```
  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DocumentPickerClass.onRequestPermissionsResult(this, permissions, grantResults);
    }
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

