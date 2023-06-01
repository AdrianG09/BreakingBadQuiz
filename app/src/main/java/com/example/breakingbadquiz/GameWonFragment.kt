package com.example.breakingbadquiz

import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.breakingbadquiz.databinding.FragmentGameWonBinding

class GameWonFragment : Fragment() {

    private var _binding: FragmentGameWonBinding? = null
    private val binding get() = _binding!!
    lateinit var player: MediaPlayer

    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameWonBinding.inflate(inflater, container, false)
        val rootView = binding.root

        binding.attemptsText.text = "You had ${viewModel.incorrectAnswers} wrong answers"

        setHasOptionsMenu(true)

        player = MediaPlayer.create(context, R.raw.fortnitelobby)
        player.isLooping = true
        player.start()

        binding.pauseButton.setOnClickListener {
            if(player.isPlaying) {
                player.pause()
                binding.pauseButton.setImageResource(R.drawable.ic_play)
            }
            else {
                player.start()
                binding.pauseButton.setImageResource(R.drawable.ic_pause)
            }
        }

        binding.fastForwardButton.setOnClickListener {
            player.seekTo(player.currentPosition + 10000)
        }

        binding.rewindButton.setOnClickListener {
            player.seekTo(player.currentPosition - 10000)
        }

        return rootView
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

}