package com.example.bemyplant.module

import com.example.bemyplant.model.UserModel
import io.realm.annotations.RealmModule

@RealmModule(classes = [UserModel::class])
class UserModule {
}