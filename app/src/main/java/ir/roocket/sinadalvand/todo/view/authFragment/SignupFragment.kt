package ir.roocket.sinadalvand.todo.view.authFragment

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import ir.roocket.sinadalvand.todo.R
import ir.roocket.sinadalvand.todo.utils.Extension.snack
import ir.roocket.sinadalvand.todo.view.AuthActivity
import ir.roocket.sinadalvand.todo.view.MainActivity
import ir.roocket.sinadalvand.todo.viewmodel.AuthViewModel
import it.sephiroth.android.library.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.fragment_signup.*
import java.io.File
import java.net.URI


class SignupFragment : Fragment(), View.OnClickListener, NumberPicker.OnNumberPickerChangeListener {

    private lateinit var model: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = (requireActivity() as AuthActivity).model
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fragment_signup_edittext_name.setText(model.name)
        fragment_signup_numberpicker_age.setProgress(model.age,true)
        if(model.picture!=null)
            Glide.with(requireContext()).load(model.picture).into(fragment_signup_imageview_picker)


        fragment_signup_imageview_picker.setOnClickListener(this)
        fragment_signup_button_submit.setOnClickListener(this)


        fragment_signup_edittext_name.doOnTextChanged { text, _, _, _ ->
            model.name = text.toString()
        }

        fragment_signup_numberpicker_age.numberPickerChangeListener = this

        model.loginResult.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(requireContext(), getString(R.string.you_have_logged_in), Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(),MainActivity::class.java))
                requireActivity().finish()
            }else{
                snack(getString(R.string.error))
            }
        }
    }


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!! as Uri
                Glide.with(this).load(fileUri).into(fragment_signup_imageview_picker)
                model.picture = fileUri.toFile()
            }
        }

    private fun pickImage() {
        ImagePicker.with(this)
            .cropSquare()
            .compress(450)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    override fun onClick(v: View?) {
        when (v) {
            fragment_signup_button_submit -> {
                if (NameAgePictureCheck()) {
                    model.register()
                }
            }
            fragment_signup_imageview_picker -> {
                pickImage()
            }
        }

    }

    private fun NameAgePictureCheck(): Boolean {
        if (model.picture == null) {
            Snackbar.make(requireView(), getString(R.string.choose_a_picture), Snackbar.LENGTH_LONG)
                .setAction("Browse") {
                    pickImage()
                }.show()
            return false
        }


        if (model.name.isEmpty()) {
            Snackbar.make(requireView(), getString(R.string.enter_your_name), Snackbar.LENGTH_LONG).show()
            return false
        }

        if (model.age <= 0) {
            Snackbar.make(requireView(), getString(R.string.how_old_are_you), Snackbar.LENGTH_LONG).show()
            return false
        }

        return true
    }

    override fun onProgressChanged(numberPicker: NumberPicker, progress: Int, fromUser: Boolean) {
        model.age = progress
    }

    override fun onStartTrackingTouch(numberPicker: NumberPicker) {

    }

    override fun onStopTrackingTouch(numberPicker: NumberPicker) {

    }
}