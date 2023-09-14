package com.example.sevenminworkoutapp

object Constants {

    fun defaultExerciseList(): ArrayList<ExerciseModel>{
        val exerciseList = ArrayList<ExerciseModel>()
        val jumpingRope = ExerciseModel(
            1,
            "Jumping rope",
            R.drawable.ic_jumping_rope,
            false,
            false,
            600000
        )
        exerciseList.add(jumpingRope)

        val squat = ExerciseModel(
            2,
            "Squat",
            R.drawable.ic_squat,
            false,
            false,
            60000
        )
        exerciseList.add(squat)

        val pushUp = ExerciseModel(
            3,
            "Push Up",
            R.drawable.ic_pushup,
            false,
            false,
            60000
        )
        exerciseList.add(pushUp)

        val abdominalCrunch = ExerciseModel(
            4,
            "Abdominal Crunch",
            R.drawable.ic_crunches,
            false,
            false,
            60000
        )
        exerciseList.add(abdominalCrunch)

        val shadowBoxing1 = ExerciseModel(
            5,
            "Shadow boxing",
            R.drawable.ic_shadow_boxing,
            false,
            false,
            180000
        )
        exerciseList.add(shadowBoxing1)

        val shadowBoxing2 = ExerciseModel(
            6,
            "Shadow boxing",
            R.drawable.ic_shadow_boxing,
            false,
            false,
            180000
        )
        exerciseList.add(shadowBoxing2)

        val shadowBoxing3 = ExerciseModel(
            7,
            "Shadow boxing",
            R.drawable.ic_shadow_boxing,
            false,
            false,
            180000
        )
        exerciseList.add(shadowBoxing3)

        val heavyBagBoxing1 = ExerciseModel(
            8,
            "Heavy bag boxing",
            R.drawable.ic_heavy_bag_boxing,
            false,
            false,
            180000
        )
        exerciseList.add(heavyBagBoxing1)

        val heavyBagBoxing2 = ExerciseModel(
            9,
            "Heavy bag boxing",
            R.drawable.ic_heavy_bag_boxing,
            false,
            false,
            180000
        )
        exerciseList.add(heavyBagBoxing2)

        val heavyBagBoxing3 = ExerciseModel(
            10,
            "Heavy bag boxing",
            R.drawable.ic_heavy_bag_boxing,
            false,
            false,
            180000
        )
        exerciseList.add(heavyBagBoxing3)


        return exerciseList

    }

}