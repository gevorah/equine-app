package co.edu.icesi.dev.petscued.view.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.edu.icesi.dev.petscued.R
import co.edu.icesi.dev.petscued.model.Questions

class QuestionAdapter (val questionList: List<Questions>):
    RecyclerView.Adapter<QuestionAdapter.QuestionVH>(){

    class QuestionVH(itemView:View): RecyclerView.ViewHolder(itemView){

        var questionNametxt : TextView = itemView.findViewById(R.id.questionName)
        var descriptiontxt : TextView = itemView.findViewById(R.id.description)
        var linearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout)
        var expandibleLayout : RelativeLayout= itemView.findViewById(R.id.expandible_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionVH {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.fila_help, parent, false)

        return QuestionVH(view)
    }

    override fun onBindViewHolder(holder: QuestionVH, position: Int) {
        val questions : Questions = questionList[position]
        holder.questionNametxt.text = questions.questionName
        holder.descriptiontxt.text = questions.description

        val isExpandible: Boolean= questionList[position].expandible
        holder.expandibleLayout.visibility = if(isExpandible) View.VISIBLE else View.GONE

        holder.linearLayout.setOnClickListener{
            val questions= questionList[position]
            questions.expandible = !questions.expandible
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return questionList.size
    }
}