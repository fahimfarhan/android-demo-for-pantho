package com.example.pantho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.pantho.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


/*
        if(_binding != null) {
            // todo logic
        }
        _binding?.base?.let {
            // logic
        }
*/

        binding.btn.setOnClickListener {
            val fragmentA = FragmentA()  // FragmentA fa = new FragmentA();

            supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragmentA, FragmentA.TAG)
                .addToBackStack(FragmentA.TAG)
                .commit()
        }
    }
}