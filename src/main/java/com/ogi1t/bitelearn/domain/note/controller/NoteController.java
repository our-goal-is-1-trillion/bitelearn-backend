package com.ogi1t.bitelearn.domain.note.controller;

import com.ogi1t.bitelearn.domain.learning.entity.enums.Category;
import com.ogi1t.bitelearn.domain.note.dto.response.IncorrectNoteDetailResponse;
import com.ogi1t.bitelearn.domain.note.dto.response.IncorrectNoteListResponse;
import com.ogi1t.bitelearn.domain.note.service.NoteService;
import com.ogi1t.bitelearn.global.security.CustomPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Note API", description = "오답 노트 및 저장한 글 API")
@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

  private final NoteService noteService;

  @Operation(summary = "오답 노트 목록 조회 (무한 스크롤)", description = "틀린 문제(오답) 목록을 카테고리별로 10개씩 조회합니다. 첫 요청 시 cursor는 비워두세요.")
  @GetMapping("/incorrect")
  public ResponseEntity<IncorrectNoteListResponse> getIncorrectNotes(
      @RequestParam(required = false) Category category, // 카테고리가 없으면 전체 조회
      @RequestParam(required = false) Long cursor,
      @AuthenticationPrincipal CustomPrincipal principal) {

    IncorrectNoteListResponse response = noteService.getIncorrectNoteList(principal.getUserId(), category, cursor);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "오답 노트 상세 조회", description = "특정 오답 노트의 문제 내용, 내 오답, 정답, 해설을 조회합니다.")
  @GetMapping("/incorrect/{noteId}")
  public ResponseEntity<IncorrectNoteDetailResponse> getIncorrectNoteDetail(
      @PathVariable Long noteId,
      @AuthenticationPrincipal CustomPrincipal principal) {

    IncorrectNoteDetailResponse response = noteService.getIncorrectNoteDetail(principal.getUserId(), noteId);
    return ResponseEntity.ok(response);
  }
}