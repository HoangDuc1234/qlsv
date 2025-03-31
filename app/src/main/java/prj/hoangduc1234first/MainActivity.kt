package prj.hoangduc1234first

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var studentAdapter: StudentAdapter
    private val studentList = mutableListOf<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val btnAdd = findViewById<FloatingActionButton>(R.id.btnAdd)

        studentAdapter = StudentAdapter(studentList) { position -> showDeleteDialog(position) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = studentAdapter

        btnAdd.setOnClickListener { showAddStudentDialog() }
    }

    private fun showAddStudentDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_student, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etMSSV = dialogView.findViewById<EditText>(R.id.etMSSV)

        AlertDialog.Builder(this)
            .setTitle("Thêm Sinh Viên")
            .setView(dialogView)
            .setPositiveButton("Thêm") { _, _ ->
                val name = etName.text.toString()
                val mssv = etMSSV.text.toString()
                if (name.isNotEmpty() && mssv.isNotEmpty()) {
                    studentList.add(0, Student(name, mssv))
                    studentAdapter.notifyItemInserted(0)
                } else {
                    Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showDeleteDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Xóa sinh viên")
            .setMessage("Bạn có chắc muốn xóa sinh viên này?")
            .setPositiveButton("Xóa") { _, _ ->
                studentList.removeAt(position)
                studentAdapter.notifyItemRemoved(position)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}
