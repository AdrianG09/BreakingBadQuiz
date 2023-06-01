package com.example.breakingbadquiz

import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.breakingbadquiz.databinding.FragmentMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by activityViewModels()

    lateinit var player: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val rootView = binding.root

        player = MediaPlayer.create(context, R.raw.correct)

        binding.trueButton.setOnClickListener {
            checkAnswer(true)
        }
        binding.falseButton.setOnClickListener {
            checkAnswer(false)
        }
        binding.advanceButton.setOnClickListener { viewModel.nextQuestion() }
        binding.previousButton.setOnClickListener { viewModel.lastQuestion() }
        binding.questionText.setOnClickListener { viewModel.nextQuestion() }

        binding.cheatButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToCheatFragment()
            rootView.findNavController().navigate(action)
        }

        viewModel.currentIndex.observe(viewLifecycleOwner) {
            binding.questionText.text = getString(viewModel.currentQuestionText)
        }

        viewModel.gameWon.observe(viewLifecycleOwner) { gameWonStatus ->
            if (gameWonStatus == true) {
                MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.title)).setMessage(getString(R.string.message_text)).setPositiveButton(getString(R.string.yes_text)) { dialog, which ->
                    viewModel.playAgain()
                }.setNegativeButton(getString(R.string.no_text)) { dialog, which ->
                    val action = MainFragmentDirections.actionMainFragmentToGameWonFragment()
                    rootView.findNavController().navigate(action)
                }.show()
            }
        }

        viewModel.nextQuestion()
        viewModel.lastQuestion()

        setHasOptionsMenu(true)

        return rootView
    }

    fun checkAnswer(currentAnswer: Boolean) {
        if (viewModel.checkAnswer(currentAnswer) == false && !viewModel.currentQuestionCheatStatus) {
            Toast.makeText(activity, R.string.incorrect_toast, Toast.LENGTH_SHORT).show()
            player = MediaPlayer.create(context, R.raw.wrong)
            player.start()
        } else if (viewModel.currentQuestionCheatStatus) {
            Toast.makeText(activity, R.string.used_cheat_toast, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, R.string.correct_toast, Toast.LENGTH_SHORT).show()
            player = MediaPlayer.create(context, R.raw.correct)
            player.start()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }

}

