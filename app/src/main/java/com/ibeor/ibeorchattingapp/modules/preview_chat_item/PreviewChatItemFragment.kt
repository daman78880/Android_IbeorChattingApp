package com.ibeor.ibeorchattingapp.modules.preview_chat_item

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.giphy.sdk.core.GPHCore
import com.giphy.sdk.core.models.enums.RenditionType
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentChatBinding
import com.ibeor.ibeorchattingapp.databinding.FragmentPreviewChatBinding
import kotlinx.coroutines.flow.combine

class PreviewChatFragment : Fragment() {
    private var binding:FragmentPreviewChatBinding?=null
 override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding=FragmentPreviewChatBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.apply {
            var type = arguments?.getInt("type")
            if(type == 3){
                val id=arguments?.getString("id")
                vedioPlayerPreviewImage.visibility=View.GONE
                gifViewPreviewImage.visibility=View.GONE
                imgUserImage.visibility=View.VISIBLE
                Log.i("inlasdfn","link is ${id}")
                Glide.with(requireContext()).load(id!!).into(imgUserImage)

            }
            else if(type == 4){
                val id=arguments?.getString("id")
                vedioPlayerPreviewImage.visibility=View.VISIBLE
                gifViewPreviewImage.visibility=View.GONE
                imgUserImage.visibility=View.GONE
                val player = ExoPlayer.Builder(requireContext()).build()
                vedioPlayerPreviewImage.player = player
                val mediaItemm = MediaItem.fromUri(id!!)
                player.setMediaItem(mediaItemm)
                player.prepare()
            }

            else if (type == 5) {
                vedioPlayerPreviewImage.visibility=View.GONE
                imgUserImage.visibility=View.GONE
                gifViewPreviewImage.visibility=View.VISIBLE

                val id=arguments?.getString("id")
                GPHCore.gifById(id!!) { result, e ->
                    gifViewPreviewImage.setMedia(result?.data, RenditionType.original)
                    e?.let {
                        //your code here
                    }
                }
            }
            imgCancelPreviewImage.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
 }