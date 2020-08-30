package com.example.developerslife

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class HotTab : Fragment() {

    companion object {
        fun newInstance() = HotTab()
    }

    private lateinit var viewModel: HotTabViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.hot_tab_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(HotTabViewModel::class.java)
        val imageView: ImageView = root.findViewById(R.id.hot_gif)
        val textView: TextView = root.findViewById(R.id.hot_description)
        val buttonNext: ImageButton = root.findViewById(R.id.button_next_hot)
        val buttonPrevious: ImageButton = root.findViewById(R.id.button_previous_hot)
        val errorMessage: TextView = root.findViewById(R.id.error_text_hot)
        var currentGif = 0
        var flagDone = false
        var isError = false
        var listOfGifs: ArrayList<GifProperty> = ArrayList()
        viewModel.description.observe(viewLifecycleOwner, Observer { new ->
            textView.setText(new)
        })


        viewModel.response.observe(viewLifecycleOwner, Observer { new ->
                listOfGifs.addAll(ArrayList(new))
                if (listOfGifs.size != 0) {
                    Glide.with(this)
                        .load(listOfGifs[currentGif].gifUrlSource)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(Glide.with(this).load(R.drawable.loading_image).fitCenter())
                        .centerCrop()
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                isError = true
                                textView.visibility = View.GONE
                                imageView.visibility = View.GONE
                                buttonNext.visibility = View.GONE
                                errorMessage.visibility = View.VISIBLE
                                errorMessage.setText("Отсутствует подключение к интернету!\n Пожалуйста, повторите позже")
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                        })
                        .into(imageView)
                    textView.setText(listOfGifs[currentGif].description)
                    flagDone = true
                } else {
                    textView.setText(getString(R.string.no_gif_message).toString())
                    Glide.with(context!!)
                        .load(R.drawable.fail_to_load_gif)
                        .centerCrop()
                        .into(imageView)
                    buttonNext.visibility = View.GONE
                }

        })


        buttonNext.setOnClickListener {
            if (flagDone) {
                currentGif++
                if (currentGif == listOfGifs.size) {
                    viewModel.getGif()
                    flagDone = false
                } else {
                    Glide.with(this)
                        .load(listOfGifs[currentGif].gifUrlSource)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(Glide.with(this).load(R.drawable.loading_image))
                        .into(imageView)
                    textView.setText(listOfGifs[currentGif].description)
                }
                if (listOfGifs.size != 0) {
                    buttonPrevious.visibility = View.VISIBLE
                }
            }
        }

        buttonPrevious.setOnClickListener{
            currentGif--
            Glide.with(this)
                .load(listOfGifs[currentGif].gifUrlSource)
                .onlyRetrieveFromCache(true)
                .into(imageView)
            textView.setText(listOfGifs[currentGif].description)
            if (currentGif == 0) {
                buttonPrevious.visibility = View.GONE
            }
        }
        return root
    }

    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val n = cm.activeNetwork
            if (n != null) {
                val nc = cm.getNetworkCapabilities(n)
                //It will check for both wifi and cellular network
                return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI)
            }
            return false
        } else {
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HotTabViewModel::class.java)
        // TODO: Use the ViewModel
        viewModel.getGif()
    }


}