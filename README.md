# SportBuff

VideoBuff Lib instruction :

1.) Import VideoOverlayLib and slidetounlock in gradle file.

2.) Declare "VidOverlayLayout" (fragment) in xml over video view.
    
    <fragment
        android:id="@+id/fragOverlay"
        android:name="com.tecocraft.videooverlaylib.VidOverlayLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        />

3.) Initialize VidOverlayLayout in activity.

	videoOverlay = fmManager.findFragmentById(R.id.fragOverlay) as VidOverlayLayout

4.) Then apply overlay content(profile pic, name, etc...).
 
 	 videoOverlay.setQuestionBuffVisibility(View.VISIBLE) //Visibilty
	 videoOverlay.setPlayerProfile(model.result.author.image) //Image Url
	 videoOverlay.setPlayerName(name) //Name
	 videoOverlay.countDownTimer(15) //Time in sec
 	 videoOverlay.setQuestion(que) //Question

5.) Now create answer list and apply to recycleview(id:rvAns).

	videoOverlay.rvAns....(Recycleview)


For more information, Please Check Example Project (SportBuff App)

Download Test APK [SportBuff.apk](https://github.com/kajalgondaliya/SportBuff/blob/master/APK/Sport%20Buff%20App.apk?raw=true)