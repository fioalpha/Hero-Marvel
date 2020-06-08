package br.com.fioalpha.heromarvel.presentation.listcharacters.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import br.com.fioalpha.heromarvel.R
import br.com.fioalpha.heromarvel.core.imageManager.ImageManger
import br.com.fioalpha.heromarvel.presentation.listcharacters.presentation.model.CharacterViewData
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CharactersAdapter(
    private val imageManger: ImageManger,
    private val actionFavoriteSelected: (CharacterViewData, Int) -> Unit,
    private val actionItemSelected: (CharacterViewData) -> Unit
) : RecyclerView.Adapter<CharactersAdapter.CharacterViewHolder>() {

    private val data: MutableList<CharacterViewData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.character_item_view, parent, false)
            .run {
                CharacterViewHolder(this, imageManger, actionFavoriteSelected, actionItemSelected)
            }
    }

    override fun getItemCount(): Int = data.count()

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun loadData(data: List<CharacterViewData>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun updateData(data: CharacterViewData, position: Int) {
        this.data[position] = data
        notifyItemChanged(position)
    }

    fun remove(position: Int) {
        this.data.removeAt(position)
        notifyDataSetChanged()
    }

    fun add(data: List<CharacterViewData>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class CharacterViewHolder(
        view: View,
        private val imageManger: ImageManger,
        private val actionFavorite: (CharacterViewData, Int) -> Unit,
        private val actionItemSelected: (CharacterViewData) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        fun bind(data: CharacterViewData) {
            with(itemView) {
                imageManger.loaderImage(data.imagePath, findViewById<ImageView>(R.id.character_adapter_image))
                findViewById<TextView>(R.id.character_adapter_title).text = data.title
                setOnClickListener { actionItemSelected(data) }
                findViewById<FloatingActionButton>(R.id.character_adapter_favorite)
                    .apply {
                        setOnClickListener { actionFavorite(data, adapterPosition) }
                        setImageResource(data.handleFavorite())
                    }
            }
        }
    }
}

@DrawableRes
fun CharacterViewData.handleFavorite(): Int {
    return if (favorite) R.drawable.favorite
        else R.drawable.un_favorite
}
