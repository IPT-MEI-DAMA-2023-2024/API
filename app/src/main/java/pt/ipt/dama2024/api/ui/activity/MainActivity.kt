package pt.ipt.dama2024.api.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import pt.ipt.dama2024.api.R
import pt.ipt.dama2024.api.model.Note
import pt.ipt.dama2024.api.retrofit.RetrofitInitializer
import pt.ipt.dama2024.api.ui.adpter.NoteListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.GregorianCalendar
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    // 'pointer' to the button
    private lateinit var bt: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // start the read of API data, and use it
        listNotes()

        // catch the 'click' event
        bt=findViewById(R.id.btNewNote)
        bt.setOnClickListener {
            addNewNote()
        }
    }

    /**
     * create a random note     *
     */
    private fun addNewNote() {
        val i = Random(GregorianCalendar.getInstance().timeInMillis).nextInt(100)
        val note = Note("Note " + i, "Description $i")

        addNote(note) {
            Toast.makeText(this, "Added " + it?.description, Toast.LENGTH_LONG).show()
            listNotes()

        }
    }

    /**
     * function that really add the note to API
     *
     * @param note
     * @param function
     * @receiver
     */
    private fun addNote(note: Note, onResult: (Note?) -> Unit) {
        val call = RetrofitInitializer().noteService().addNote(note)
        call.enqueue(
            object : Callback<Note> {
                /**
                 * Invoked for a received HTTP response.
                 * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
                 * Call [Response.isSuccessful] to determine if the response indicates success.
                 */
                override fun onResponse(call: Call<Note>, response: Response<Note>) {
                    val addedNote = response.body()
                    onResult(addedNote)
                }

                /**
                 * Invoked when a network exception occurred talking to the server or when an unexpected exception
                 * occurred creating the request or processing the response.
                 */
                override fun onFailure(call: Call<Note>, t: Throwable) {
                    t.printStackTrace()
                    onResult(null)
                }
            }
        )
    }

    private fun listNotes() {
        // here we are actually reading the API data
        val call = RetrofitInitializer().noteService().getNotes()
        // do something with the received data
        processList(call)
    }

    /**
     * Process list that was provided by API
     *
     * @param call list of notes
     */
    private fun processList(call: Call<List<Note>>) {
        call.enqueue(object : Callback<List<Note>?> {
            override fun onResponse(call: Call<List<Note>?>?,
                                    response: Response<List<Note>?>?) {
                response?.body()?.let {
                    val notes: List<Note> = it
                     configureList(notes)
                }
            }
            override fun onFailure(call: Call<List<Note>?>?, t: Throwable?) {
                t?.printStackTrace()
                t?.message?.let { Log.e("onFailure error", it) }
            }
        })
    }

    private fun configureList(notes: List<Note>) {
        val recyclerView: RecyclerView = findViewById(R.id.nodeList)
        recyclerView.adapter = NoteListAdapter(notes, this)
        // how the card will be organized
        val layoutManager = StaggeredGridLayoutManager(
            3, StaggeredGridLayoutManager.VERTICAL
        )
        recyclerView.layoutManager = layoutManager
    }
}