package com.example.myattendance

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.myattendance.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //expand view to the background of statusbar
        if (Build.VERSION.SDK_INT in 21..29) {
            window.statusBarColor = Color.TRANSPARENT
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility =
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE

        } else if (Build.VERSION.SDK_INT >= 30) {
            window.statusBarColor = Color.TRANSPARENT
            // Making status bar overlaps with the activity
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }

//        val uri = intent?.data
//        val userId = uri?.getQueryParameter("userId")
//
//        if(userId!=null){
//            Log.d("MainActivity userId","user-> ${userId.toInt()}")
//            if(userId.toInt()!=0){
//                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
//                val navController = navHostFragment.navController
//                navController.navigate(R.id.loginFragment)
//            }
//        }


//        if(userId!=0){
//            // Find the NavController for the target graph
//            val navController = findNavController(R.id.nav_graph)
//            navController.navigate(R.id.loginFragment)
//        }

        /*
*  Making the Navigation system bar not overlapping with the activity
*/
//        if (Build.VERSION.SDK_INT >= 30) {

            // Root ViewGroup of my activity

//            ViewCompat.setOnApplyWindowInsetsListener(binding.navHostFragmentActivityMain) { view, windowInsets ->
//
//                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
//
//                // Apply the insets as a margin to the view. Here the system is setting
//                // only the bottom, left, and right dimensions, but apply whichever insets are
//                // appropriate to your layout. You can also update the view padding
//                // if that's more appropriate.
//
//                view.layoutParams =  (view.layoutParams as FrameLayout.LayoutParams).apply {
//                    leftMargin = insets.left
//                    bottomMargin = insets.bottom
//                    rightMargin = insets.right
//                    topMargin = insets.top
//                }
//
//                // Return CONSUMED if you don't want want the window insets to keep being
//                // passed down to descendant views.
//                WindowInsetsCompat.CONSUMED
//            }

//        }

    }

}