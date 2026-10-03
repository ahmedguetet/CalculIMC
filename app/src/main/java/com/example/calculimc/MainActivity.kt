package com.example.calculimc

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

/**
 * Activité principale de l'application de calcul de l'IMC.
 *
 * L'utilisateur saisit son poids (kg) et sa taille (m). L'application
 * calcule l'indice de masse corporelle, l'affiche avec deux décimales
 * et indique la catégorie correspondante avec une couleur adaptée.
 */
class MainActivity : AppCompatActivity() {

    // Composants graphiques.
    private lateinit var editTextPoids: EditText
    private lateinit var editTextTaille: EditText
    private lateinit var buttonCalculer: Button
    private lateinit var buttonEffacer: Button
    private lateinit var textViewImc: TextView
    private lateinit var textViewCategorie: TextView

    // Clés pour conserver le dernier résultat après rotation (amélioration).
    private val CLE_IMC = "cle_imc"
    private val CLE_CATEGORIE = "cle_categorie"
    private val CLE_COULEUR = "cle_couleur"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Récupération des composants à partir de leurs identifiants.
        editTextPoids = findViewById(R.id.editTextPoids)
        editTextTaille = findViewById(R.id.editTextTaille)
        buttonCalculer = findViewById(R.id.buttonCalculer)
        buttonEffacer = findViewById(R.id.buttonEffacer)
        textViewImc = findViewById(R.id.textViewImc)
        textViewCategorie = findViewById(R.id.textViewCategorie)

        buttonCalculer.setOnClickListener { calculerImc() }
        buttonEffacer.setOnClickListener { effacer() }

        // Amélioration : restaurer le dernier résultat après rotation de l'écran.
        if (savedInstanceState != null) {
            textViewImc.text = savedInstanceState.getString(CLE_IMC, "")
            textViewCategorie.text = savedInstanceState.getString(CLE_CATEGORIE, "")
            textViewCategorie.setTextColor(savedInstanceState.getInt(CLE_COULEUR, ContextCompat.getColor(this, R.color.noir)))
        }
    }

    /**
     * Récupère les saisies, contrôle les données, calcule l'IMC
     * puis affiche la valeur, la catégorie et la couleur adaptée.
     */
    private fun calculerImc() {
        val poidsTexte = editTextPoids.text.toString().trim().replace(',', '.')
        val tailleTexte = editTextTaille.text.toString().trim().replace(',', '.')

        // 2. Vérifier que les deux champs sont renseignés.
        if (poidsTexte.isEmpty() || tailleTexte.isEmpty()) {
            Toast.makeText(this, getString(R.string.erreur_champs_vides), Toast.LENGTH_SHORT).show()
            return
        }

        // 3. Convertir les valeurs en nombres décimaux.
        val poids = poidsTexte.toDoubleOrNull()
        val taille = tailleTexte.toDoubleOrNull()

        if (poids == null || taille == null) {
            Toast.makeText(this, getString(R.string.erreur_valeur_invalide), Toast.LENGTH_SHORT).show()
            return
        }

        // 4. Vérifier que poids et taille sont strictement positifs.
        if (poids <= 0 || taille <= 0) {
            Toast.makeText(this, getString(R.string.erreur_valeur_positive), Toast.LENGTH_SHORT).show()
            return
        }

        // 5. Calculer l'IMC. 6. Arrondir à deux décimales.
        val imc = poids / (taille * taille)
        val imcArrondi = String.format("%.2f", imc)
        textViewImc.text = getString(R.string.format_imc, imcArrondi)

        // 7. Déterminer la catégorie avec if / else if / else.
        val categorie: String
        val couleurRes: Int
        if (imc < 18.5) {
            categorie = getString(R.string.cat_insuffisance)
            couleurRes = R.color.orange
        } else if (imc < 25) {
            categorie = getString(R.string.cat_normale)
            couleurRes = R.color.vert
        } else if (imc < 30) {
            categorie = getString(R.string.cat_surpoids)
            couleurRes = R.color.orange
        } else if (imc < 35) {
            categorie = getString(R.string.cat_obesite_moderee)
            couleurRes = R.color.rouge
        } else if (imc < 40) {
            categorie = getString(R.string.cat_obesite_severe)
            couleurRes = R.color.rouge
        } else {
            categorie = getString(R.string.cat_obesite_morbide)
            couleurRes = R.color.rouge_fonce
        }

        // 8. Afficher la catégorie avec la couleur adaptée.
        textViewCategorie.text = categorie
        textViewCategorie.setTextColor(ContextCompat.getColor(this, couleurRes))
    }

    /**
     * Bouton Effacer : vide les champs, supprime l'ancien résultat
     * et replace le curseur dans le champ du poids.
     */
    private fun effacer() {
        editTextPoids.text.clear()
        editTextTaille.text.clear()
        textViewImc.text = ""
        textViewCategorie.text = ""
        editTextPoids.requestFocus()
    }

    /**
     * Amélioration : conserver le dernier résultat après rotation.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CLE_IMC, textViewImc.text.toString())
        outState.putString(CLE_CATEGORIE, textViewCategorie.text.toString())
        outState.putInt(CLE_COULEUR, textViewCategorie.currentTextColor)
    }
}
