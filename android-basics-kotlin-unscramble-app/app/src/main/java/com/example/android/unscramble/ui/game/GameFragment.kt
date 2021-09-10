/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.example.android.unscramble.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment where the game is played, contains the game logic.
 */
class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModels()

    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        // re created will call every first time fragment is created
        Log.d("GameFragment", "GameFragment created/re-created!")

        // show the log to show score and the other
        Log.d(
            "GameFragment", "Word: ${viewModel.currentScrambledWord} " +
                    "Score: ${viewModel.score} WordCount: ${viewModel.currentWordCount}"
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }

        // add view model to layout
        binding.gameViewModel = viewModel

        // add max of word to layout variable
        binding.maxNoOfWords = MAX_NO_OF_WORDS

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner
    }

    /*
    * Checks the user's word, and updates the score accordingly.
    * Displays the next scrambled word.
    */
    private fun onSubmitWord() {
        // the guessed word by player
        val playerWord = binding.textInputEditText.text.toString()
        // if user guessed word is correct
        if (viewModel.isUserWordCorrect(playerWord)) {
            // set error text field to false
            setErrorTextField(false)
            // if there was no empty slot
            if (!viewModel.nextWord()) {
                showFinalScoreDialog()
            }
            // if user guessed word is false
        } else {
            // set error text field to true
            setErrorTextField(true)
        }
    }

    /*
    * Skips the current word without changing the score.
    */
    private fun onSkipWord() {
        // if there was still has a empty slot
        if (viewModel.nextWord()) {
            // set error text field to false
            setErrorTextField(false)
        } else {
            // or show alert dialog
            showFinalScoreDialog()
        }
    }

    /*
     * Re-initializes the data in the ViewModel and updates the views with the new data, to
     * restart the game.
     */
    private fun restartGame() {
        // reset the data in view model
        viewModel.reinitializeData()
        // delete error text field
        setErrorTextField(false)
    }

    /*
     * Exits the game.
     */
    private fun exitGame() {
        activity?.finish()
    }

    /*
    * Sets and resets the text field error status.
    */
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            // set error enable is true
            binding.textField.isErrorEnabled = true
            // set the text error
            binding.textField.error = getString(R.string.try_again)
        } else {
            // or set error enable is false
            binding.textField.isErrorEnabled = false
            // set the text error null
            binding.textInputEditText.text = null
        }
    }

    /*
    * Creates and shows an AlertDialog with the final score.
    */
    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
            // set title to alert dialog
            .setTitle(getString(R.string.congratulations))
            // set message that from view model
            .setMessage(getString(R.string.you_scored, viewModel.score.value))
            // alert dialog cannot be canceled
            .setCancelable(false)
            // set negative button to exit game
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            // set positive button to restart game
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame()
            }
            // show the alert dialog
            .show()
    }
}
