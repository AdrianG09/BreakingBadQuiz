package com.example.breakingbadquiz

import android.media.MediaPlayer
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController

class QuizViewModel : ViewModel() {

    var questionList = listOf(
        Question(R.string.question_one, true, false),
        Question(R.string.question_two, false, false),
        Question(R.string.question_three, false, false),
        Question(R.string.question_four, false, false),
        Question(R.string.question_five, true, false)
    )

    private val _gameWon = MutableLiveData(false)
    val gameWon: LiveData<Boolean>
        get() = _gameWon

    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int>
        get() = _currentIndex

    private var _incorrectAnswers = 0
    val incorrectAnswers: Int
        get() = _incorrectAnswers

    private var _correctAnswers = 0
    val correctAnswers: Int
        get() = _correctAnswers

    val currentQuestionAnswer: Boolean
        get() = questionList[_currentIndex.value ?: 0].questionAnswer

    val currentQuestionText: Int
        get() = questionList[_currentIndex.value ?: 0].questionID

    val currentQuestionCheatStatus: Boolean
        get() = questionList[_currentIndex.value ?: 0].usedCheating

    fun setCheatedStatusForCurrentQuestion(cheated: Boolean) {
        questionList[_currentIndex.value ?: 0].usedCheating = cheated
    }

    fun checkIfGameWon() {
        if (_correctAnswers >= 3) {
            _gameWon.value = true
        }
    }

    fun nextQuestion() {
        if (_currentIndex.value == questionList.lastIndex) {
            _currentIndex.value = 0
        } else {
            _currentIndex.value = _currentIndex.value?.plus(1)
        }
    }

    fun lastQuestion() {
        if (_currentIndex.value == 0) {
            _currentIndex.value = questionList.lastIndex
        } else {
            _currentIndex.value = _currentIndex.value?.minus(1)
        }
    }

    fun checkAnswer(currentAnswer: Boolean): Boolean {
        if (currentAnswer != currentQuestionAnswer) {
            _incorrectAnswers++
            return false
        } else if (currentQuestionCheatStatus) {
            return false
        } else {
            _correctAnswers++
            checkIfGameWon()
            return true

        }
    }

    fun playAgain() {
        _incorrectAnswers = 0
        _correctAnswers = 0
        _currentIndex.value = 0
        for (question in questionList) {
            question.usedCheating = false
        }
    }
}

