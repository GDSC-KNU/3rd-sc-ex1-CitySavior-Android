package com.citysavior.android.presentation.main.map

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.citysavior.android.domain.model.report.Category
import com.citysavior.android.domain.model.report.Point
import com.citysavior.android.domain.params.report.CreateReportParams
import com.citysavior.android.presentation.common.component.CustomTextEditField
import com.citysavior.android.presentation.common.constant.Colors
import com.citysavior.android.presentation.common.constant.Sizes
import com.citysavior.android.presentation.common.constant.TextStyles
import com.citysavior.android.presentation.common.utils.noRippleClickable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CreateReportScreen(
    onBackIconClick : () -> Unit = {},
    onUploadButtonClick : (CreateReportParams) -> Unit = {},
) {
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)




    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }



    var photoUri : Uri? = null

    val takeCameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()){
        if(it){
            Timber.d("사진 저장 성공 ${photoUri.toString()}")
            capturedImageUri = photoUri!!
            val contentValues = ContentValues().apply {
                val imageFileName = "CITY_SAVIOR_${System.currentTimeMillis()}.jpg"
                put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            }

            val resolver: ContentResolver = context.contentResolver

            // Insert the image into the MediaStore
            val externalUri: Uri? =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            // Write the image data to the MediaStore
            externalUri?.let { externalMediaUri ->
                resolver.openOutputStream(externalMediaUri)?.use { outputStream ->
                    resolver.openInputStream(photoUri!!)?.copyTo(outputStream)
                }
            }
        }else{
            Timber.d("사진 저장 실패")
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){
        if(it != null){
            capturedImageUri = it
        }else{
            Timber.d("사진 가져오기 실패")
        }

    }

    var description by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ){
        TopAppBar(
            modifier = Modifier.background(Color.White),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
            ),
            title = {
                Text(
                    text = "신고하기",
                    style = TextStyles.TITLE_MEDIUM2,
                )
            },
            navigationIcon = {
                Icon(
                    modifier = Modifier.noRippleClickable { onBackIconClick() },
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null
                )
            },
        )
        Text(
            text = "Upload Image",
            style = TextStyles.TITLE_MEDIUM2,
        )
        Spacer(modifier = Modifier.height(20.dp))
        if(capturedImageUri == Uri.EMPTY) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
                    .background(Colors.WIDGET_BG_GREY)
            )
        }else{
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f),
                painter = rememberAsyncImagePainter(capturedImageUri),
                contentScale = ContentScale.FillHeight,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row{
            TextButton(
                shape = RoundedCornerShape(Sizes.WIDGET_ROUND),
                modifier = Modifier
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(Sizes.WIDGET_ROUND),
                    )
                    .background(
                        color = Colors.PRIMARY_BLUE,
                        shape = RoundedCornerShape(Sizes.WIDGET_ROUND)
                    )
                    .weight(1f),
                contentPadding = PaddingValues(
                    vertical = 16.dp,
                ),
                onClick = {
                    if (cameraPermissionState.status.isGranted) {
                        Timber.d("카메라 권한 허용됨")
                        val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

                        val photoFile = File.createTempFile(
                            "selected_image",
                            ".jpg",
                            directory,
                        ) // 해당 폴더에 임시 파일을 만든다.
                        photoUri = FileProvider.getUriForFile(
                            context,
                            context.packageName + ".provider",
                            photoFile
                        )

                        takeCameraLauncher.launch(photoUri)
                    } else {
                        cameraPermissionState.launchPermissionRequest()
                    }
                },
            ) {
                Text(
                    text = "Take photo",
                    color = Color.White,
                    style = TextStyles.CONTENT_SMALL1_STYLE,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            TextButton(
                shape = RoundedCornerShape(Sizes.WIDGET_ROUND),
                modifier = Modifier
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(Sizes.WIDGET_ROUND),
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(Sizes.WIDGET_ROUND)
                    )
                    .weight(1f),
                contentPadding = PaddingValues(
                    vertical = 16.dp,
                ),
                onClick = {
                    galleryLauncher.launch("image/*")
                },
            ) {
                Text(
                    text = "Browse gallery",
                    color = Colors.BLACK,
                    style = TextStyles.CONTENT_SMALL1_STYLE,
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Add description",
            style = TextStyles.CONTENT_TEXT3_STYLE,
        )
        Spacer(modifier = Modifier.height(20.dp))

        CustomTextEditField(
            modifier = Modifier.background(
                color = Colors.WIDGET_BG_GREY, shape = RoundedCornerShape(Sizes.WIDGET_ROUND)
            ),
            value = description,
            onValueChange = { description = it },
            backgroundColor = Color.Transparent,
            startPadding = 12.dp,
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Select Category",
            style = TextStyles.CONTENT_TEXT3_STYLE,
        )

        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            shape = RoundedCornerShape(Sizes.WIDGET_ROUND),
            modifier = Modifier
                .background(
                    color = Colors.PRIMARY_BLUE,
                    shape = RoundedCornerShape(Sizes.WIDGET_ROUND)
                )
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = 50.dp,
                vertical = 12.dp,
            ),
            onClick = {
                val file = getFileFromContentUri(context, capturedImageUri)
                if(file != null){
                    val params = CreateReportParams(
                        file = file,
                        point = Point.fixture(),
                        description = description,
                        category = Category.AIR_QUALITY
                    )
                    onUploadButtonClick(params)
                }
            },
        ) {
            Text(
                text = "Upload",
                color = Color.White,
                style = TextStyles.TITLE_MEDIUM2,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

fun getFileFromContentUri(context: Context, uri: Uri): File? {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri)
    val tempFile = File.createTempFile("temp", null, context.cacheDir)
    tempFile.deleteOnExit()
    inputStream?.use { input ->
        FileOutputStream(tempFile).use { output ->
            input.copyTo(output)
        }
    }
    return tempFile
}


