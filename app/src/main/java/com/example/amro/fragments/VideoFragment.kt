package com.example.amro.fragments


import com.example.amro.R
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import kotlinx.android.synthetic.main.fragment_video.*


class VideoFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //playVideo()
    }


    /*private fun  playVideo() {
        val  mc = MediaController(baseActivity)
        videoView.setMediaController(mc)
        val path = "android.resource://" + baseActivity!!.packageName + "/" + R.raw.amro
        val  uri = Uri.parse(path)
        videoView.setVideoURI(uri)
        videoView.start()

    }*/
}
