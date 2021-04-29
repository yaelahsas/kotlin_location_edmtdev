package com.karina.tracking

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.karina.tracking.Model.Usernya
import com.karina.tracking.Utils.Common
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.paperdb.Paper
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        private val MY_REQ_CODE = 7117
    }

    lateinit var user_information: DatabaseReference
    lateinit var providers: List<AuthUI.IdpConfig>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //init papper
        Paper.init(this)

        //init firebase
        user_information = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION)

        //init provider
        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()

        )

        //Request Permission
        Dexter.withContext(this)
            .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    showSignOption()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    TODO("Not yet implemented")
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                    Toast.makeText(this@MainActivity, "Perlu Akses", Toast.LENGTH_LONG).show()

                }

            }).check()
    }

    private fun showSignOption() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(), MY_REQ_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQ_CODE) {
            val firebaseUser = FirebaseAuth.getInstance().currentUser!!
            //periksa apakah user ada di database
            user_information.orderByKey()
                .equalTo(firebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.value == null) {
                            //user tidak ada di database
                            if (!snapshot.child(firebaseUser.uid).exists()) {
                                Common.loggedUser = Usernya(firebaseUser.uid, firebaseUser.email!!)
                                //tambahkan user ke databse
                                user_information.child(Common.loggedUser.uid!!)
                                    .setValue(Common.loggedUser)
                            }
                        } else {
                            //user ada di database
                            Common.loggedUser = snapshot.child(firebaseUser.uid)
                                .getValue(Usernya::class.java)!!
                        }

                        //simpan uid ke storage untuk update lokasi dari node yang dihapus
                        Paper.book().write(Common.USER_UID_SAVE_KEY, Common.loggedUser.uid)
                        updateToker(firebaseUser)
                        setupUI()
                    }

                })
        }
    }

    private fun setupUI() {
        //setelah roses selesai indah intent
        startActivity(Intent(this@MainActivity, HomeActivity::class.java))
        finish()
    }

    private fun updateToker(firebaseUser: FirebaseUser?) {
        val tokens = FirebaseDatabase.getInstance()
            .getReference(Common.TOKENS)

        //ambil tokenya
        //deprecated
//        FirebaseInstanceId.getInstance().instanceId
//                .addOnSuccessListener {
//                    tokens.child(firebaseUser!!.uid)
//                            .setValue(it.token)
//                }.addOnFailureListener {
//                    Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_LONG).show()
//                }

        //new
        FirebaseInstallations.getInstance().id
            .addOnSuccessListener {
                tokens.child(firebaseUser!!.uid)
                    .setValue(FirebaseMessaging.getInstance().token)
            }.addOnFailureListener {
                Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_LONG).show()
            }
    }
}