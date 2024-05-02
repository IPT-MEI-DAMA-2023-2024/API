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
import pt.ipt.dama2024.api.model.Note2API
import pt.ipt.dama2024.api.retrofit.RetrofitInitializer
import pt.ipt.dama2024.api.ui.adpter.NoteListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.GregorianCalendar
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    // 'pointer' to the button
    private lateinit var bt1: Button
    private lateinit var bt2: Button

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

        // catch the 'click' event of first button
        bt1 = findViewById(R.id.btNewNote)
        bt1.setOnClickListener {
            addNewNote(1)
        }
        // catch the 'click' event of second button
        bt2 = findViewById(R.id.btNewNote2)
        bt2.setOnClickListener {
            addNewNote(2)
        }

    }

    /**
     * create a random note
     * @param option 1-use the previous code
     *               2-use the new code
     */
    private fun addNewNote(option:Int) {
        val i = Random(GregorianCalendar.getInstance().timeInMillis).nextInt(100)
        val note = Note("Note " + i, "Description $i ($option option)")

        if(option==1) {
            addNote(note) {
                Toast.makeText(this, "note added by first code", Toast.LENGTH_LONG).show()
                listNotes()
            }
        }else{
            addNote2(note) {
                Toast.makeText(this, "Added by second code", Toast.LENGTH_LONG).show()
                listNotes()
            }
        }
    }

    /**
     * function that really add the note to API
     *
     * @param note
     * @param onResult
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

    /**
     * function that really add the note to API
     *
     * @param note
     * @param onResult
     * @receiver
     */
    private fun addNote2(note: Note, onResult: (Note2API?) -> Unit) {
        val call = RetrofitInitializer().noteService().addNote2API(note)
        call.enqueue(
            object : Callback<Note2API> {
                /**
                 * Invoked for a received HTTP response.
                 * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
                 * Call [Response.isSuccessful] to determine if the response indicates success.
                 */
                override fun onResponse(call: Call<Note2API>, response: Response<Note2API>) {
                    val addedNote = response.body()
                    onResult(addedNote)
                }

                /**
                 * Invoked when a network exception occurred talking to the server or when an unexpected exception
                 * occurred creating the request or processing the response.
                 */
                override fun onFailure(call: Call<Note2API>, t: Throwable) {
                    t.printStackTrace()
                    onResult(null)
                }
            }
        )
    }


    /**
     * Access API to get a list of notes
     *
     */
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
            override fun onResponse(
                call: Call<List<Note>?>?,
                response: Response<List<Note>?>?
            ) {
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