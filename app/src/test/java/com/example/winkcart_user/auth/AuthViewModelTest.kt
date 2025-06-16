

import com.example.winkcart_user.data.repository.FirebaseRepo
import com.example.winkcart_user.data.repository.ProductRepoImpl
import com.example.winkcart_user.auth.AuthViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test


import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelValidationTest {

    private lateinit var repo: FirebaseRepo
    private lateinit var productRepo: ProductRepoImpl
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        repo = mockk(relaxed = true)
        productRepo = mockk(relaxed = true)
        viewModel = AuthViewModel(repo, productRepo)
    }


    @Test
    fun `isEmailValid should return true for valid email`() {
        val validEmail = "test@example.com"
        val result = viewModel.isEmailValid(validEmail)
        assertTrue(result)
    }

    @Test
    fun `isEmailValid should return false for invalid email`() {
        val invalidEmail = "invalid-email"
        val result = viewModel.isEmailValid(invalidEmail)
        assertFalse(result)
    }

    @Test
    fun `isPasswordValid should return true for valid password`() {
        val validPassword = "Test@123"
        val result = viewModel.isPasswordValid(validPassword)
        assertTrue(result)
    }

    @Test
    fun `isPasswordValid should return false for short password`() {
        val shortPassword = "T@1a"
        val result = viewModel.isPasswordValid(shortPassword)
        assertFalse(result)
    }

    @Test
    fun `isPasswordValid should return false for password missing special character`() {
        val weakPassword = "Test1234"
        val result = viewModel.isPasswordValid(weakPassword)
        assertFalse(result)
    }



    @Test
    fun `signIn should return true on successful login`() {
        // Arrange
        val email = "test@example.com"
        val password = "Test@123"
        val mockTask = mockk<Task<AuthResult>> {
            every { isSuccessful } returns true
            every { addOnCompleteListener(any()) } answers {
                firstArg<OnCompleteListener<AuthResult>>().onComplete(this@mockk)
                this@mockk
            }
        }
        every { repo.signInFireBase(email, password) } returns mockTask
        var result: Boolean? = null
        viewModel.signIn(email, password) {
            result = it
        }
        assertEquals(true, result)
        verify(exactly = 1) { repo.signInFireBase(email, password) }
    }

    @Test
    fun `signOut should call signOutFireBase on repo`() {
        every { repo.signOutFireBase() } returns Unit
        viewModel.signOut()
        verify(exactly = 1) { repo.signOutFireBase() }
    }

}

