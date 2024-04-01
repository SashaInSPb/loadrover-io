package loadrover.api.io.infra

import com.amazonaws.services.s3.model.AmazonS3Exception
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.S3Object
import com.amazonaws.services.s3.model.S3ObjectInputStream
import com.amazonaws.util.IOUtils
import loadrover.api.io.config.AwsConfig
import loadrover.api.io.config.AwsS3Properties
import loadrover.api.io.config.exception.BaseException
import loadrover.api.io.config.exception.ExceptionCode
import loadrover.api.io.domain.base.BaseDto
import nonapi.io.github.classgraph.utils.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream

@Service
class AwsS3Service (
    private val awsConfig: AwsConfig,
    private val awsS3Properties: AwsS3Properties
) {
    private val logger = LoggerFactory.getLogger(AwsS3Service::class.java)

    fun upload(uploadPath: String, file: InputStream): BaseDto.UploadResultDto {
        val bytes: ByteArray = IOUtils.toByteArray(file)
        val byteArrayInPutStream = ByteArrayInputStream(bytes)

        try {
            awsConfig.amazonS3Client().putObject(
                awsS3Properties.s3.bucket,
                uploadPath,
                byteArrayInPutStream,
                ObjectMetadata()
            )

        } catch (e: Exception) {
            logger.error("Failed to upload file to S3: ${e.message.toString()}")
            throw BaseException(ExceptionCode.UPLOAD_FAIL)
        }


        println("---upload file info start---")
        println(uploadPath)
        println("${awsS3Properties.s3.cloudFrontDomain}/${uploadPath}")
        println(awsConfig.amazonS3Client().getUrl(awsS3Properties.s3.bucket, uploadPath).toString())
        println("---upload file info end---")

        return BaseDto.UploadResultDto(
            fullPath = "https://${awsS3Properties.s3.cloudFrontDomain}/${uploadPath}",
            path = "${awsS3Properties.s3.cloudFrontDomain}/${uploadPath}"
        )
    }

//    fun getObject(filePath: String): File {

        // 다운로드 파일 경로
//        val downloadFilePath = "${System.getProperty("java.io.tmpdir")}/${FilenameUtils.getName(key)}"
//        val downloadFile = File(downloadFilePath)
//
//        try {
//            val s3Object: S3Object = awsConfig.amazonS3Client().getObject(awsS3Properties.s3.bucket, filePath)
//            val inputStream: S3ObjectInputStream = s3Object.objectContent
//
//            FileUtils.copyInputStreamToFile(inputStream, downloadFile)
//
//        } catch (e: AmazonS3Exception) {
//            throw IllegalArgumentException(e.toString())
//        } catch (e: Exception) {
//            throw java.lang.IllegalArgumentException(e.toString())
//        }

//    }

    fun rename(source: String, target: String) {
        if (source == target) {
            return
        }
        if (source.isEmpty() || target.isEmpty()) {
            return
        }

        awsConfig.amazonS3Client().copyObject(awsS3Properties.s3.bucket, source, awsS3Properties.s3.bucket, target)
    }

    fun delete(source: String) {
        awsConfig.amazonS3Client().deleteObject(awsS3Properties.s3.bucket, source)
    }



}