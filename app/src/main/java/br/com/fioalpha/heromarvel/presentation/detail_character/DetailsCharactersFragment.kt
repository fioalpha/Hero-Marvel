package br.com.fioalpha.heromarvel.presentation.detail_character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.fioalpha.heromarvel.R
import br.com.fioalpha.heromarvel.core.imageManager.ImageManger
import br.com.fioalpha.heromarvel.core.utils.showWarning
import br.com.fioalpha.heromarvel.presentation.detail_character.model.CharacterDetailViewData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class DetailsCharactersFragment: Fragment(R.layout.detail_character), DetailsView {

    private val arguments: DetailsCharactersFragmentArgs by navArgs()

    private val imageManger: ImageManger by inject()

    private var imageView: ImageView? = null

    private var title: TextView? = null

    private var description: TextView? = null

    private var favoriteButton: FloatingActionButton? = null

    private val presenter: DetailPresenter by inject {
        parametersOf(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return setupView(inflater, container)
    }

    private fun setupView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        val view = inflater.inflate(R.layout.detail_character, container, false)
        with(view) {
            imageView = findViewById(R.id.detail_character_image)
            title = findViewById(R.id.detail_character_title)
            description = findViewById(R.id.detail_character_description)
            favoriteButton = findViewById(R.id.detail_character_favorite)
            favoriteButton?.setOnClickListener { presenter.handleClickFavorite() }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.loadData(
            CharacterDetailViewData.transform(arguments.character)
        )
    }

    override fun showData(character: CharacterDetailViewData) {
        imageView?.let { imageManger.loaderImage(character.imagePath, it) }
        title?.text = character.name
        description?.text = character.descriptions
        presenter.handleStatusFavorite()
    }

    override fun updateFavorite(favorite: Int) {
        favoriteButton?.setImageResource(favorite)
    }

    override fun showError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showWarning(message: String) {
        activity.showWarning(message)
    }
}
