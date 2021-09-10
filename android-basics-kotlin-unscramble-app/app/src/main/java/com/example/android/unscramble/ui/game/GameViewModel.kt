package com.example.android.unscramble.ui.game

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    // list of word
    private var wordsList: MutableList<String> = mutableListOf()

    // score guessed word with live data
    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    // current word variable with live data
    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    // change to livedata variable
    private val _currentScrambledWord = MutableLiveData<String>()

    // using spannable instead of string
    val currentScrambledWord: LiveData<Spannable> = Transformations.map(_currentScrambledWord) {
        if (it == null) {
            SpannableString("")
        } else {
            val scrambledWord = it.toString()
            val spannable: Spannable = SpannableString(scrambledWord)
            spannable.setSpan(
                TtsSpan.VerbatimBuilder(scrambledWord).build(),
                0,
                scrambledWord.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannable
        }
    }

    // to hold a word that current use
    private lateinit var currentWord: String

    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    /*
        *get the random word and set to current word variable
     */
    private fun getNextWord() {
        // get the random word
        currentWord = allWordsList.random()
        // change the current word to Char Array
        val tempWord = currentWord.toCharArray()
        // and then shuffle the char array
        tempWord.shuffle()

        // shuffle the word again if the shuffled word is the same as current word
        while (tempWord.toString().equals(currentWord, false)) {
            tempWord.shuffle()
        }

        // if teh wordlist contain the current word then move to the next word
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            // set value to _currentScrambledWord variable with shuffle word
            _currentScrambledWord.value = String(tempWord)
            // increase the number of currentWordCount variable
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            // add current wor to wordsList variable
            wordsList.add(currentWord)
        }
    }

    /*
    * Returns true if the current word count is less than MAX_NO_OF_WORDS.
    * Updates the next word.
    */
    fun nextWord(): Boolean {
        // if the currentWordCount value is less than MAX_NO_OF_WORDS
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            // go on
            getNextWord()
            true
        } else false
    }
    
    /*
    * increase the score
    */
    private fun increaseScore() {
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

    /*
    * to check if the guessed word by player is correct
    */
    fun isUserWordCorrect(playerWord: String): Boolean {
        // if player word is equal with currentWord variable
        if (playerWord.equals(currentWord, true)) {
            // then increase the score
            increaseScore()
            return true
        }
        return false
    }

    /*
    * Re-initializes the game data to restart the game.
    */
    fun reinitializeData() {
        // set the score to zero
        _score.value = 0
        // set the currentWordCount to zero
        _currentWordCount.value = 0
        // clear the list of word
        wordsList.clear()
        // get the new word
        getNextWord()
    }
}