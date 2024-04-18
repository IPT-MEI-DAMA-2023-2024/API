package pt.ipt.dama2024.api.ui.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipt.dama2024.api.R
import pt.ipt.dama2024.api.model.Note

class NoteListAdapter(
    private val notes: List<Note>,
    private val context: Context
) : RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder?.let {
            it.bindView(note)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    /**
     * atribui os valores da 'note' ao objeto que os vai representar na interface
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * takes each Note (that was read from API) and add it to the CardView
         */
        fun bindView(note: Note) {
            val title: TextView = itemView.findViewById(R.id.noteItem_title)
            val description: TextView = itemView.findViewById(R.id.noteItem_description)

            title.text = note.title
            description.text = note.description
        }
    }
}
