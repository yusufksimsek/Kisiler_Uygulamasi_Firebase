package com.example.kisiler_uygulamasi_firebase

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.kisiler_uygulamasi_firebase.databinding.KisiCardTasarimBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference

class KisilerAdapter(private val mContext: Context,
                     private val kisilerListe: List<Kisiler>,
                     private val refKisiler: DatabaseReference,                                      )
    :RecyclerView.Adapter<KisilerAdapter.CardTasarimTutucu>() {

          inner class CardTasarimTutucu(private val binding: KisiCardTasarimBinding) :
              RecyclerView.ViewHolder(binding.root) {

              @SuppressLint("SuspiciousIndentation")
              fun bind(kisi: Kisiler) {
                  binding.tvKisiBilgi.text = "${kisi.kisi_ad} - ${kisi.kisi_tel}"

                  binding.imageView.setOnClickListener {
                    val popupmenu = PopupMenu(mContext,binding.imageView)
                      popupmenu.menuInflater.inflate(R.menu.popup_menu,popupmenu.menu)

                      popupmenu.setOnMenuItemClickListener { menuItem ->
                          when(menuItem.itemId){
                              R.id.action_sil -> {
                                  Snackbar.make(binding.imageView,"${kisi.kisi_ad} silinsin mi?",Snackbar.LENGTH_SHORT)
                                      .setAction("EVET"){
                                        refKisiler.child(kisi.kisi_id!!).removeValue()
                                      }.show()
                                  true
                              }
                              R.id.action_duzenle -> {
                                  alertGoster(kisi)
                                  true
                              }
                              else -> false
                          }
                      }

                      popupmenu.show()
                  }
              }

              fun alertGoster(kisi: Kisiler){
                  val tasarim = LayoutInflater.from(mContext).inflate(R.layout.alert_tasarim,null)
                  val editTextAd = tasarim.findViewById(R.id.etAd) as EditText
                  val editTextTel = tasarim.findViewById(R.id.etTel) as EditText

                  editTextAd.setText(kisi.kisi_ad)
                  editTextTel.setText(kisi.kisi_tel)

                  val ad = AlertDialog.Builder(mContext)
                  ad.setTitle("Kisi Guncelle")
                  ad.setView(tasarim)

                  ad.setPositiveButton("Guncelle"){ dialogInterface, i ->
                      val kisi_ad = editTextAd.text.toString().trim()
                      val kisi_tel = editTextTel.text.toString().trim()

                      val bilgiler = HashMap<String,Any>()
                      bilgiler.put("kisi_ad",kisi_ad)
                      bilgiler.put("kisi_tel",kisi_tel)

                      refKisiler.child(kisi.kisi_id!!).updateChildren(bilgiler)

                      Toast.makeText(mContext,"$kisi_ad - $kisi_tel",Toast.LENGTH_SHORT).show()
                  }
                  ad.setNegativeButton("Iptal"){ dialogInterface, i ->

                  }
                  ad.create().show()
              }

          }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val binding = KisiCardTasarimBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardTasarimTutucu(binding)
    }

    override fun getItemCount(): Int {
        return kisilerListe.size
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        holder.bind(kisilerListe[position])
    }

}