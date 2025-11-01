package com.example.kotlintest2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.res.ResourcesCompat
import android.text.Html

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupText: TextView

    companion object {
        private const val PREFS_NAME = "QcumbeRPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USER_EMAIL = "userEmail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 이미 로그인되어 있는지 확인
        if (isUserLoggedIn()) {
            navigateToMainActivity()
            return
        }

        setContentView(R.layout.activity_login2)

        // 뷰 초기화
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        signupText = findViewById(R.id.signupText)

        // 로고 투톤 색상 적용
        val logoTextView = findViewById<TextView>(R.id.appTitle)
        val logoText = "QcumbeR"
        val spannableString = SpannableString(logoText)

        logoTextView.typeface = ResourcesCompat.getFont(this, R.font.quantico_bold)

        spannableString.setSpan(
            ForegroundColorSpan(getColor(R.color.dark_green)),
            0, 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(getColor(R.color.dark_green)),
            6, 7,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        logoTextView.text = spannableString

        // 회원가입 밑줄 추가
        signupText.text = Html.fromHtml(
            "계정이 없으신가요? <u>회원가입</u>",
            Html.FROM_HTML_MODE_LEGACY
        )

        // 로그인 버튼 클릭 리스너
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // 입력 검증
            if (email.isEmpty()) {
                Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 로그인 수행
            performLogin(email, password)
        }

        // 회원가입 화면으로 이동
        signupText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    private fun saveLoginState(email: String) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_EMAIL, email)
            apply()
        }
    }

    private fun performLogin(email: String, password: String) {
        // 임시로 어떤 입력이든 로그인 성공으로 처리
        // 추후 Firebase 인증으로 교체 예정

        // 로그인 상태 저장
        saveLoginState(email)

        Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()

        // 메인 액티비티로 이동
        navigateToMainActivity()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}