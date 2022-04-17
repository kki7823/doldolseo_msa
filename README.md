# doldolseo_msa

## About
서울시의 각 지역을 소개하고, 일상 과 서울 여행 후기들을 공유하는 웹 커뮤니티 '돌고돌아서울' 입니다.
기존에 진행한 ['돌고돌아서울'](https://github.com/kki7823/doldolseo)을 모놀리식 아키텍처에서 MSA로 재구성한 프로젝트 입니다. 해당 소스코드는 '돌고돌아서울' MSA의 백엔드 코드 입니다.<br/>
[프론트코드 보기](https://github.com/kki7823/doldolseo_vite)

## Environment
Java : 8 </br>
Spring-boot : 2.6.4

## Structure
#### 1. common : 공통으로 사용하는 모듈입니다. 각종 유틸들을 공유합니다. 
#### 2. api-*: 각 영역별 api 서비스 모듈입니다. 
  - api-area : 지역게시판 api 모듈 
  - api-review : 후기게시판 api 모듈 
  - api-member : 회원 api 모듈 
  - api-crew : 크루 api 모듈 
  - api-crew-post : 크루 활동 게시판 api 모듈 
#### 3. gateway : api 게이트웨이 입니다. 
