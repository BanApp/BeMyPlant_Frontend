package com.example.bemyplant.data

data class LoginData(
    val username: String,
    val password: String
)
data class LoginResponse(
    val token: String
)
data class SignUpData(
    val username: String,
    val password: String,
    val phones: String,
    val r_name: String,
    val cre_date: String,
    val activated: Int = 1
)
data class SignUpResponse(
    val username: String,
    val r_name: String,
    val phones: String,
    val cre_date: String,
    val authorityDtoSet:  List<AuthorityDto>
)



data class AuthorityDto(
    val authorityName: String
)

/*data class withdrawalResponse(
    val message: String
)*/

data class UserData(
    val username: String,
    val r_name: String,
    val phones: String,
    val cre_date: String,
    val authorityDtoSet:  List<AuthorityDto>
)


data class SensorData(
    val id: String,
    val airTemp: Double,
    val airHumid: Double,
    val soilHumid: Double,
    val lightIntensity: Double,
    val status: Boolean,
    val date: String
)


data class ChatRequest(
    val input_text: String
)

data class ChatResponse(
    val response: String
)

data class StatusResponse(
    val name : String,
    val weather: String,
    val temperature: Double,
    val humidity: Int,
    val status: Int,
    val most_important_feature: String
)

data class StatusData(
    val city: String
)

data class GeneratePlantImageRequest(
    val color: String,
    val species: String,
    val pot_color: String
)


data class GeneratePlantImageResponse (
    val plant_image_urls: List<String>
)

data class GenerateUserImageRequest (
    val gender: String,
    val characteristic: String,
)


data class GenerateUserImageResponse (
    val user_image_urls: List<String>
)

data class GardenResponse(
    val cntntsNo: String, //컨텐츠 번호
    val plntbneNm: String, //식물학 명
    val plntzrNm: String, //식물영 명
    val distbNmval : String, //유통 명
    val fmlNm : String, //과 명
    val fmlCodeNm : String, //과 코드 명
    val  orgplceInfo : String, //원산지 정보
    val adviseInfo : String, //조언 정보
    val imageEvlLinkCours : String, //이미지 평가 링크 경로
    val growthHgInfo : String, //성장 높이 정보
    val growthAraInfo : String, //성장 넓이 정보
    val lefStleInfo : String, //잎 형태 정보
    val smellCode : String, //냄새 코드
    val smellCodeNm : String, //냄새 코드 명
    val toxctyInfo : String, //독성 정보
    val prpgtEraInfo : String, //번식 시기 정보
    val etcEraInfo : String, //기타 시기 정보
    val managelevelCode : String, //관리 수준 코드
    val managelevelCodeNm : String, //관리 수준 코드명
    val grwtveCode : String, //생장 속도 코드
    val grwtveCodeNm : String, //생장 속도 코드명
    val grwhTpCode : String, //생육 온도 코드
    val grwhTpCodeNm : String, //생육 온도 코드명
    val winterLwetTpCode : String, //겨울 최저 온도 코드
    val winterLwetTpCodeNm : String, //겨울 최저 온도 코드면
    val hdCode : String, //습도 코드
    val hdCodeNm : String, //습도 코드명
    val frtlzrInfo : String, //비료 정보
    val soilInfo : String, //토양 정보
    val watercycleSprngCode : String, //물주기 봄 코드
    val watercycleSprngCodeNm : String, //물주기 봄 코드명
    val watercycleSummerCode : String, //물주기 여름 코드
    val watercycleSummerCodeNm : String, //물주기 여름 코드명
    val watercycleAutumnCode : String, //물주기 가을 코드
    val watercycleAutumnCodeNm : String, //물주기 가을 코드명
    val watercycleWinterCode : String, //물주기 겨울 코드
    val watercycleWinterCodeNm : String, //물주기 겨울 코드명
    val dlthtsManageInfo : String, //병충해 관리 정보
    val speclmanageInfo : String, //특별 관리 정보
    val fncltyInfo : String, //기능성 정보
    val flpodmtBigInfo : String, //화분직경 대 정보
    val flpodmtMddlInfo : String, //화분직경 중 정보
    val flpodmtSmallInfo : String, //화분직경 소 정보
    val WIDTH_BIG_INFO : String, //가로 대 정보
    val widthMddlInfov : String, //가로 중 정보
    val  widthSmallInfo : String, //가로 소 정보
    val vrticlBigInfo : String, //세로 대 정보
    val vrticlMddlInfo : String, //세로 중 정보
    val vrticlSmallInfo : String, //세로 소 정보
    val hgBigInfo : String, //높이 대 정보
    val hgMddlInfo : String, //높이 중 정보
    val hgSmallInfo : String, //높이 소 정보
    val volmeBigInfo : String, //볼륨 대 정보
    val volmeMddlInfo : String, //볼륨 중 정보
    val volmeSmallInfo : String, //볼륨 소 정보
    val pcBigInfo : String, //가격 대 정보
    val pcMddlInfo : String, //가격 중 정보
    val pcSmallInfo : String, //가격 소 정보
    val managedemanddoCode : String, //관리요구도 코드
    val managedemanddoCodeNm : String, //관리요구도 코드명
    val clCode : String, //분류 코드(콤마(,)로 구분)
    val clCodeNm : String, //분류 코드명(콤마(,)로 구분)
    val grwhstleCode : String, //생육형태 코드(콤마(,)로 구분)
    val grwhstleCodeNm : String, //생육형태 코드명(콤마(,)로 구분)
    val indoorpsncpacompositionCode : String, //실내정원구성 코드(콤마(,)로 구분)
    val indoorpsncpacompositionCodeNm : String, //실내정원구성 코드명(콤마(,)로 구분)
    val  eclgyCode : String, //생태 코드(콤마(,)로 구분)
    val eclgyCodeNm : String, //생태 코드명(콤마(,)로 구분)
    val lefmrkCode : String, //잎무늬 코드(콤마(,)로 구분)
    val lefmrkCodeNm : String, //잎무늬 코드명(콤마(,)로 구분)
    val lefcolrCode : String, //잎색 코드(콤마(,)로 구분)
    val lefcolrCodeNm : String, //잎색 코드명(콤마(,)로 구분)
    val ignSeasonCode : String, //발화 계절 코드(콤마(,)로 구분)
    val ignSeasonCodeNm : String, //발화 계절 코드명(콤마(,)로 구분)
    val flclrCode : String, //꽃색 코드(콤마(,)로 구분)
    val flclrCodeNm : String, //꽃색 코드명(콤마(,)로 구분)
    val fmldeSeasonCode : String, //과일 계절(콤마(,)로 구분)
    val fmldeSeasonCodeNm : String, //과일 계절(콤마(,)로 구분)
    val fmldecolrCode : String, //과일색 코드(콤마(,)로 구분)
    val fmldecolrCodeNm : String, //과일색 코드명(콤마(,)로 구분)
    val prpgtmthCode : String, //번식방법 코드(콤마(,)로 구분)
    val prpgtmthCodeNm : String, //번식방법 코드명(콤마(,)로 구분)
    val lighttdemanddoCode : String, //광요구도 코드(콤마(,)로 구분)
    val lighttdemanddoCodeNm : String, //광요구도 코드명(콤마(,)로 구분)
    val postngplaceCode : String, //배치장소 코드(콤마(,)로 구분)
    val postngplaceCodeNm : String, //배치장소 코드명(콤마(,)로 구분)
    val dlthtsCode : String, //병충해 코드(콤마(,)로 구분)
    val dlthtsCodeNm : String //병충해 코드(콤마(,)로 구분)


)


