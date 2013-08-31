set (service_VERSION    "0.0.1")

include_directories (.
    ${SOURCE_DIR}/__util/
    ${SOURCE_DIR}/__xml4nlp/
    ${SOURCE_DIR}/__ltpService/)

set (service_SRC
    ${SOURCE_DIR}/__ltpService/CirService.cpp
    ${SOURCE_DIR}/__ltpService/LTPService.cpp
    ${SOURCE_DIR}/__ltpService/LTPOption.cpp
    ${SOURCE_DIR}/__ltpService/Word.cpp
    ${SOURCE_DIR}/__ltpService/LTML.cpp
    ${SOURCE_DIR}/__util/MyLib.cpp
    ${SOURCE_DIR}/__util/Base64Encoder.cpp
    ${SOURCE_DIR}/__xml4nlp/tinystr.cpp
    ${SOURCE_DIR}/__xml4nlp/tinyxml.cpp
    ${SOURCE_DIR}/__xml4nlp/tinyxmlparser.cpp
    ${SOURCE_DIR}/__xml4nlp/tinyxmlerror.cpp
    ${SOURCE_DIR}/__xml4nlp/Xml4nlp.cpp)

add_library (ltpservice ${service_SRC})

configure_file (${SOURCE_DIR}/__ltpService/LTPService.h
    ${INCLUDE_OUTPUT_PATH}/LTPService.h)

configure_file (${SOURCE_DIR}/__ltpService/CirService.h
    ${INCLUDE_OUTPUT_PATH}/CirService.h)

configure_file (${SOURCE_DIR}/__ltpService/LTPOption.h
    ${INCLUDE_OUTPUT_PATH}/LTPOption.h)

configure_file (${SOURCE_DIR}/__ltpService/Word.h
    ${INCLUDE_OUTPUT_PATH}/Word.h)

configure_file (${SOURCE_DIR}/__ltpService/LTML.h
    ${INCLUDE_OUTPUT_PATH}/LTML.h)

configure_file (${SOURCE_DIR}/__util/MyLib.h
    ${INCLUDE_OUTPUT_PATH}/MyLib.h)

configure_file (${SOURCE_DIR}/__util/Base64Encoder.h
    ${INCLUDE_OUTPUT_PATH}/Base64Encoder.h)

configure_file (${SOURCE_DIR}/__xml4nlp/Xml4nlp.h
    ${INCLUDE_OUTPUT_PATH}/Xml4nlp.h)

configure_file (${SOURCE_DIR}/__xml4nlp/tinystr.h
    ${INCLUDE_OUTPUT_PATH}/tinystr.h)

configure_file (${SOURCE_DIR}/__xml4nlp/tinyxml.h
    ${INCLUDE_OUTPUT_PATH}/tinyxml.h)
