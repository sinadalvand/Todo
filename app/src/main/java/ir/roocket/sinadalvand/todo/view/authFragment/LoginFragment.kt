package ir.roocket.sinadalvand.todo.view.authFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ir.roocket.sinadalvand.todo.R
import ir.roocket.sinadalvand.todo.utils.Extension.snack
import ir.roocket.sinadalvand.todo.view.AuthActivity
import ir.roocket.sinadalvand.todo.view.MainActivity
import ir.roocket.sinadalvand.todo.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(), View.OnClickListener {

    val model: AuthViewModel by hiltNavGraphViewModels(R.id.auth_nav)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_login_button_login.setOnClickListener(this)
        fragment_login_button_signup.setOnClickListener(this)

        fragment_login_edittext_email.setText(model.email)
        fragment_login_edittext_password.setText(model.password)


        fragment_login_edittext_email.doOnTextChanged { text, _, _, _ ->
            model.email = text.toString()
        }

        fragment_login_edittext_password.doOnTextChanged { text, _, _, _ ->
            model.password = text.toString()
        }

        model.loginResult.observe(viewLifecycleOwner) {
            if (it) {
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            } else {
                snack(getString(R.string.error))
            }
        }

    }

    override fun onClick(v: View?) {
        when (v) {
            fragment_login_button_login -> {
                if (checkEmailAndPassword())
                    model.login()
            }
            fragment_login_button_signup -> {
                if (checkEmailAndPassword())
                    findNavController().navigate(R.id.signupFragment)
            }
        }
    }

    private fun checkEmailAndPassword(): Boolean {
        if (model.email.isEmpty()) {
            Snackbar.make(requireView(), getString(R.string.enter_email), Snackbar.LENGTH_LONG).show()
            return false
        }

        if (model.password.isEmpty()) {
            Snackbar.make(requireView(), getString(R.string.enter_password), Snackbar.LENGTH_LONG).show()
            return false
        }

        return true
    }
}