package com.example.winkcart_user.data

import com.example.winkcart_user.data.remote.RemoteDataSource
import com.example.winkcart_user.data.repository.FirebaseRepoImp
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class FirebaseRepoTest {

    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var firebaseRepo: FirebaseRepoImp

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        firebaseRepo = FirebaseRepoImp(remoteDataSource)
    }

    @Test
    fun `signInFireBase should delegate to remoteDataSource`() {
        // Arrange
        val email = "test@example.com"
        val password = "Test@123"
        val mockTask = mockk<Task<AuthResult>>(relaxed = true)

        every { remoteDataSource.signInFireBase(email, password) } returns mockTask

        // Act
        val result = firebaseRepo.signInFireBase(email, password)

        // Assert
        assertEquals(mockTask, result)
        verify(exactly = 1) { remoteDataSource.signInFireBase(email, password) }
    }

    @Test
    fun `signUpFireBase should delegate to remoteDataSource`() {
        val email = "test@example.com"
        val password = "Pass@123"
        val mockTask = mockk<Task<AuthResult>>(relaxed = true)

        every { remoteDataSource.signUpFireBase(email, password) } returns mockTask

        val result = firebaseRepo.signUpFireBase(email, password)

        assertEquals(mockTask, result)
        verify { remoteDataSource.signUpFireBase(email, password) }
    }

    @Test
    fun `firebaseAuthWithGoogle should delegate to remoteDataSource`() {
        val idToken = "fake-token"
        val mockTask = mockk<Task<AuthResult>>(relaxed = true)

        every { remoteDataSource.firebaseAuthWithGoogle(idToken) } returns mockTask

        val result = firebaseRepo.firebaseAuthWithGoogle(idToken)

        assertEquals(mockTask, result)
        verify { remoteDataSource.firebaseAuthWithGoogle(idToken) }
    }

    @Test
    fun `signOutFireBase should delegate to remoteDataSource`() {
        every { remoteDataSource.signOutFireBase() } just Runs

        firebaseRepo.signOutFireBase()

        verify { remoteDataSource.signOutFireBase() }
    }

}