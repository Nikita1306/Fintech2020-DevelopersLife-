package com.example.developerslife.ui.main

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.developerslife.Api
import com.example.developerslife.MainActivity
import com.example.developerslife.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val textView: TextView = root.findViewById(R.id.section_label)
        val imageView: ImageView = root.findViewById(R.id.imageView)
        val buttonNext: ImageButton = root.findViewById(R.id.button_next)
        val buttonPrevious: ImageButton = root.findViewById(R.id.button_previous)
        var listOfGifs: ArrayList<String> = ArrayList()
        pageViewModel.description.observe(viewLifecycleOwner, Observer { new ->
            textView.setText(new)
        })
        pageViewModel.getGif()

        pageViewModel.response.observe(viewLifecycleOwner, Observer { new ->
                listOfGifs.add(new)
                Glide.with(this)
                    .load(new)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageView)
                Log.d("TAGS", "${listOfGifs.size}")

        })
        pageViewModel.listOfGifUrls.observe(viewLifecycleOwner, Observer { new ->
           Log.d("TAGS","${new.size}")
        })
//        listOfGifs.removeAt(listOfGifs.lastIndex)
        pageViewModel.getIndex()

        buttonNext.setOnClickListener {
            pageViewModel.getGif()
        }

        buttonPrevious.setOnClickListener{
            listOfGifs.forEach { x ->
                Log.d("TAGS", x)
            }
            Log.d("TAGS", listOfGifs.last())
            Glide.with(this)
                .load(listOfGifs.get(listOfGifs.lastIndex - 1))
                .onlyRetrieveFromCache(true)
                .into(imageView)
            listOfGifs.removeAt(listOfGifs.lastIndex)
        }
        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {

            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}