# Android-Image-Resizing-Utility
[![](https://jitpack.io/v/marwia/Android-Image-Resizing-Utility.svg)](https://jitpack.io/#marwia/Android-Image-Resizing-Utility)

A simple async task to reduce JPEG file size and resolution! Useful when you have to send a JPG picture to server using Retrofit library.

## Install

Download via **Gradle**:
```gradle
dependencies {
	implementation 'com.github.marwia:Android-Image-Resizing-Utility:0.0.3'
}
```

## Usage
```Java
new ImageResizingAsyncTask(this, 1024, 1024, 1, 70).execute(file);
// wait until 'imageResizingFinish' is called

@Override
public void imageResizingFinish(byte[] byteArray) {
	//convert byte array to map and send it to server
}
```

## Example
```Java
new ImageResizingAsyncTask(this, 1024, 1024, 1, 70).execute(file);
// wait until 'imageResizingFinish' is called

@Override
public void imageResizingFinish(byte[] byteArray) {
	Map<String, RequestBody> pic1Map = prepareBodyRequest("filename.jpg", byteArray);

	final MyAPI myAPI = MyApplication.getInstance().getRetrofit().create(EblaPlusAPI.class);
	Call<Object> call2 = myAPI.uploadDocumentFile(
		pic1Map);

	call2.enqueue(new MyCallback<Object>() {
	    @Override
	    public void onResponse(Response<Object> response, Retrofit retrofit) {
		if (response.isSuccess()) {
		    //YES!!!
		} else {
		    //NOOO!!!
		}
	    }

	    @Override
	    public void onFailure(Throwable t) {
		super.onFailure(t);
		t.printStackTrace();
	    }
	});
}

private static Map<String, RequestBody> prepareBodyRequest(String filename, byte[] content) {
	Map<String, RequestBody> pic1Map = new HashMap<>();
	RequestBody rb1 = RequestBody.create(MediaType.parse("image/*"), content);

	pic1Map.put("attachment\"; filename=\"" + filename, rb1);
	return pic1Map;
}
```
