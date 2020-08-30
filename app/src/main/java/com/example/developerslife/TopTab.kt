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
import android.widget.*
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class TopTab : Fragment() {

    companion object {
        fun newInstance() = TopTab()
    }

    private lateinit var viewModel: TopTabViewModel
    private lateinit var listOfGifs: ArrayList<GifProperty>
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.top_tab_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(TopTabViewModel::class.java)
        imageView = root.findViewById(R.id.top_gif)
        textView = root.findViewById(R.id.top_description)
        val buttonNext: ImageButton = root.findViewById(R.id.button_next_top)
        val buttonPrevious: ImageButton = root.findViewById(R.id.button_previous_top)
        val errorMessage: TextView = root.findViewById(R.id.error_text_top)
        val buttonRetry: Button = root.findViewById(R.id.button_retry_top)
        var currentGif = 0
        var isError = false
        var flagDone = false
        listOfGifs = ArrayList()
        viewModel.description.observe(viewLifecycleOwner, Observer { new ->
            textView.setText(new)
        })

        viewModel.getGif()
        viewModel.response.observe(viewLifecycleOwner, Observer { new ->
                listOfGifs.addAll(ArrayList(new))
                if (listOfGifs.size != 0) {
                    isError = false
                    Glide.with(this)
                        .load(listOfGifs[currentGif].gifUrlSource)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(Glide.with(this).load(R.drawable.loading_image))
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
                                buttonRetry.visibility = View.VISIBLE
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
                    textView.setText(getString(R.string.no_gif_message))
                    Glide.with(context!!)
                        .load(R.drawable.fail_to_load_gif)
                        .centerCrop()
                        .into(imageView)
                    buttonNext.visibility = View.GONE
                }

        })

        buttonRetry.setOnClickListener{
            if (isOnline(context!!)) {
                textView.visibility = View.VISIBLE
                imageView.visibility = View.VISIBLE
                buttonNext.visibility = View.VISIBLE
                buttonRetry.visibility = View.GONE
                errorMessage.visibility = View.GONE
                isError = false
                Glide.with(this)
                    .load(listOfGifs[currentGif].gifUrlSource)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(Glide.with(this).load(R.drawable.loading_image))
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
                            buttonRetry.visibility = View.VISIBLE
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
            }
        }

        buttonNext.setOnClickListener {
                currentGif++
                if (currentGif == listOfGifs.size) {
                    viewModel.getGif()
                    flagDone = false
                }else if (currentGif >= listOfGifs.size) {
                    currentGif--
                    if (isOnline(context!!))
                        currentGif--
                    Toast.makeText(
                        context!!,
                        "Отсутствует подключение к интернету",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else {
                    isError = false
                    Glide.with(this)
                        .load(listOfGifs[currentGif].gifUrlSource)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(Glide.with(this).load(R.drawable.loading_image))
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
                                buttonRetry.visibility = View.VISIBLE
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
                }
                if (listOfGifs.size != 0) {
                    buttonPrevious.visibility = View.VISIBLE
                }
            }


        buttonPrevious.setOnClickListener{
            if (isError) {
                textView.visibility = View.VISIBLE
                imageView.visibility = View.VISIBLE
                buttonNext.visibility = View.VISIBLE
                buttonRetry.visibility = View.GONE
                errorMessage.visibility = View.GONE
            }
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
        viewModel = ViewModelProviders.of(this).get(TopTabViewModel::class.java)
        // TODO: Use the ViewModel
    }


}