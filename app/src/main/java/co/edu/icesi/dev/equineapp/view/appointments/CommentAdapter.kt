package co.edu.icesi.dev.equineapp.view.appointments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.edu.icesi.dev.equineapp.R
import co.edu.icesi.dev.equineapp.model.Comment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CommentAdapter (private val MedicalHistoryInfoFragment: MedicalHistoryInfoFragment) : RecyclerView.Adapter<CommentView>() {

    private var commentList = ArrayList<Comment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentView {
        val layoutInflater = LayoutInflater.from(parent.context)
        val comment = layoutInflater.inflate(R.layout.comment, parent, false)
        return CommentView(comment)
    }

    override fun onBindViewHolder(holder: CommentView, position: Int) {
        val comment = commentList[position]
        comment.also { holder.comment = it }

        holder.DateTextView.text= LocalDateTime.now().toString().format(DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm:ss a"))
        holder.commentTextView.text= comment.content
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    fun addComment(comment: Comment) {
        commentList.add(comment)
        notifyItemInserted(commentList.size - 1)
    }

    fun clearList(oldSize : Int) {
        commentList.clear()
        notifyItemRangeRemoved(0, oldSize)
    }
}