package com.android.developer.layout.tiptime

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.android.developer.layout.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calculateButton.setOnClickListener { calculateTip() }
        binding.costOfServiceEditText.setOnKeyListener {view, keyCode, _ -> handleKeyEvent(view, keyCode) }
    }
    /*
    * Para calcular a gorjeta, a primeira coisa de que você precisa é o custo do serviço.
    * O texto é armazenado no EditText, mas você precisa que ele seja um número para poder usá-lo em cálculos.
    * Você pode se lembrar do tipo Int de outros codelabs, mas um Int só pode conter números inteiros.
    * Para usar um número decimal no app, use o tipo de dados Double em vez de Int
    * */
    private fun calculateTip() {
        /*
        * Observe o .text no final. A primeira parte, binding.costOfService, faz referência ao elemento da IU para o custo do serviço.
        * Adicionar .text no final instrui o app a usar o resultado (um objeto EditText) e acessar a propriedade text dele.
        * Isso é conhecido como ENCADEAMENTO e é um padrão muito comum no Kotlin.
        *
        * Usando apenas o .text não funcionará. toDouble() precisa ser chamado em uma String.
        * Perceba que o atributo text de um EditText é do tipo Editable, porque representa um texto que pode ser modificado.
        * Felizmente, é possível converter um Editable em uma String chamando o método toString() nele.
        * */
        val stringInTextField = binding.costOfServiceEditText.text.toString()

        val cost = stringInTextField.toDoubleOrNull()

        if (cost == null) {
            binding.tipResult.text = ""
            return
        }

        val selectedId = binding.tipOptionsRg.checkedRadioButtonId

        /*
        * Agora você sabe que uma das opções entre
        * R.id.option_twenty_percent, R.id.option_eighteen_percent ou R.id.fifteen_percent foi o RadioButton selecionado,
        * mas precisa da porcentagem correspondente.
        * É possível escrever uma série de instruções if/else, mas é muito mais fácil usar uma expressão when
        * */

        val tipPercentage = when(selectedId) {
            R.id.option_twenty_percent_rb -> 0.20
            R.id.option_eighteen_percent_rb -> 0.18
            else -> 0.15
        }

        /*
        * Agora que você tem o custo do serviço e o percentual de gorjeta, calcular a gorjeta é simples:
        * multiplique o custo pela porcentagem da gorjeta.
        * Gorjeta = custo do serviço * porcentagem da gorjeta.
        * Esse valor pode ser arredondado para cima.
        * */
        var tip = (cost * tipPercentage)

        val roundUp = binding.roundUpSwitch.isChecked

        if (roundUp) {
            tip = kotlin.math.ceil(tip)
        }

        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)

        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }

    private fun handleKeyEvent(view : View, keyCode : Int) : Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}