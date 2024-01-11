package com.myboard.userservice.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.myboard.userservice.entity.Board;
import com.myboard.userservice.repository.BoardRepository;

@Service
public class BoardServiceImpl implements BoardService {

	private final BoardRepository boardRepository;
	private final GridFsTemplate gridFsTemplate;

	@Autowired
	public BoardServiceImpl(BoardRepository boardRepository, GridFsTemplate gridFsTemplate) {
		this.boardRepository = boardRepository;
		this.gridFsTemplate = gridFsTemplate;
	}

	@Override
	public Board saveBoard(Board board) {
		return boardRepository.save(board);
	}

	@Override
	public Board getBoardDetailsById(String boardId) {
		// Implement the logic to retrieve board details from the database or other
		// sources
		// You can use Spring Data JPA or any other data access method here
		// For simplicity, let's assume BoardRepository is a repository for board
		// entities

		// BoardRepository.findById is just a placeholder, update it based on your
		// actual data access method
		// You may also need to handle cases where the board details are not found
		// For simplicity, this example assumes that findById returns an optional Board
		return boardRepository.findById(boardId).orElse(null);
	}

	@Override
	public byte[] getImageBytes(String imageFileId) {
		if (imageFileId == null || imageFileId.isEmpty()) {
			return new byte[0];
		}

		try {
			// Retrieve the file from GridFS using the file ID
			GridFSFile gridFsFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(imageFileId)));
			if (gridFsFile != null) {
				GridFsResource resource = gridFsTemplate.getResource(gridFsFile);
				InputStream inputStream = resource.getInputStream();
				return StreamUtils.copyToByteArray(inputStream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new byte[0];
	}

}
