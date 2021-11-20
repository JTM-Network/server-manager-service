package com.jtm.server.entrypoint.controller.directory

import com.jtm.server.data.service.directory.UploadService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dir/upload")
class UploadController @Autowired constructor(private val uploadService: UploadService) {

}