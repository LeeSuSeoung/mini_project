package com.hk.board.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.hk.board.dtos.FileBoardDto;
import com.hk.board.mapper.FileMapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class FileService {

	@Autowired
	private FileMapper fileMapper;
	
	//파일업로드하기
	@Transactional
	public List<FileBoardDto> uploadFiles(String uploadPath, MultipartRequest multipartRequest) 
	        throws IllegalStateException, IOException {
	    List<MultipartFile> multipartFiles = multipartRequest.getFiles("filename");
	    List<FileBoardDto> uploadFileList = new ArrayList<>();

	    for (MultipartFile multipartFile : multipartFiles) {
	        String origin_filename = multipartFile.getOriginalFilename();
	        
	        // 원본 파일 이름이 null이거나 비어 있는지 체크
	        if (origin_filename == null || origin_filename.isEmpty()) {
	            System.out.println("Uploaded file name is invalid: " + origin_filename);
	            continue; // 파일이 없으면 다음 파일로 넘어감
	        }

	        // 저장 파일명 구하기
	        String fileExtension = origin_filename.substring(origin_filename.lastIndexOf("."));
	        String stored_filename = UUID.randomUUID().toString() + fileExtension;

	        String fileuploadUrl = uploadPath + "/" + stored_filename;
	        multipartFile.transferTo(new File(fileuploadUrl)); // upload 실행

	        uploadFileList.add(new FileBoardDto(0, 0, origin_filename, stored_filename));
	    }
	    
	    return uploadFileList;
	}


	//파일정보 가져오기
	public FileBoardDto getFileInfo(int file_seq) {
		return fileMapper.getFileInfo(file_seq);
	}

	public void fileDownload(String origin_filename, String stored_filename, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		
		//저장경로
		String filePath=request.getSession().getServletContext()
				        .getRealPath("upload");
		
		//다운로드를 위한 준비 작업
		
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition",
				            "attachment; fileName="
		                     +URLEncoder.encode(origin_filename,"UTF-8") );
		
		response.setHeader("Content-transfer", "binary");
		
		File file=new File(filePath+"/"+stored_filename);
		
		FileInputStream fs =null;// 자바로 파일 읽어들이기위한 객체
		ServletOutputStream out= null;// 출력 객체
		
		try {
			fs = new FileInputStream(file);
			out = response.getOutputStream();
			
			FileCopyUtils.copy(fs, out);// 읽고 쓰는 작업을 처리
			response.flushBuffer();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ee) {
			ee.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				out.flush();
				out.close();
				fs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}












