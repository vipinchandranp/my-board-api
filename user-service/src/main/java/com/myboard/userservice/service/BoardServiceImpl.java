package com.myboard.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myboard.userservice.entity.Board;
import com.myboard.userservice.repository.BoardRepository;

@Service
public class BoardServiceImpl implements BoardService {
	private final BoardRepository boardRepository;

	@Autowired
	public BoardServiceImpl(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	@Override
	public Board saveBoard(Board board) {
		return boardRepository.save(board);
	}
}
