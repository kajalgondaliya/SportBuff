- BuffOverlayView SDK instruction :

SDK :

- Programming Language -> Kotlin
- For Network Request -> HttpUrlConnection 
- For asynchronous programming -> Couroutine 
- For Image loading -> Picasso
- For Swipe Button Animation -> MotionLayout 
- For Serialization -> Gson
- For Unit Testing -> JUnit4, Mockito

App :

- Programming Language -> Kotlin
- App architecture - MVVM
- As observable data holder for the ViewModel - LiveData
- For Unit Testing -> JUnit4, Mockito
- For UI Testing - Expresso 

Usage :

1.) Initialize BuffOverlayView in your Application class by calling the initialize() function.

	- BuffOverlayView.initialize("")


2.) The SDK exposes a custom view called BuffOverlay, which is to be integrated into the host application using Relative Layout as parent layout.

	-  <com.tecocraft.buffoverlayview.view.BuffOverlay
        android:id="@+id/buffView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentStart="true"
        android:layout_alignParentBottom="true"
      />


3.) Call start() function on the BuffOverlayView instance to start fetching and displaying Buffs.

 	-  buffView.start(this)


4.) Setup event listeners for your BuffView by calling addListener.

	- buffView.addListener(object : EventListener {
            override fun onBuffDisplayed(buff: Buff) {
                super.onBuffDisplayed(buff)
            }

            override fun onBuffAnswerSelected(answer: Buff.Answer) {
                super.onBuffAnswerSelected(answer)
                showToast("Your Answer is : ${answer.title}")
            }

            override fun onBuffError(t: Throwable) {
                super.onBuffError(t)
                showToast("Error : ${t.message}")
            }
        }) 



Customization BuffOverlayView:

 <com.tecocraft.buffoverlayview.view.BuffOverlay
        android:id="@+id/buffView"    
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentStart="true"
        android:layout_alignParentBottom="true"
        app:buff_author_info_text_color="@color/white"                              //default to Black
        app:buff_answer_row_background="@drawable/buff_question_answer_layout_bg"   //default to light gray with alpha
        app:buff_answer_row_text_color="@color/white"                               //default to Black 
        app:buff_author_info_background="@drawable/buff_author_layout_bg"           //default to light gray with alpha
        app:buff_question_background="@drawable/buff_question_layout_bg"            //default to Black with alpha
        app:buff_selected_answer_row_text_color="@color/colorPrimary"               //default to White
        />


Test Cases:

- Test cases for App and SDK.


// For More Information Please Check Code. //