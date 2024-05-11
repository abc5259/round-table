import { useState } from "react";
import { StyleSheet, Text, View } from "react-native";
import { Calendar, LocaleConfig } from "react-native-calendars";
import Button from "../../atoms/Button/Button";
import Modal from "react-native-modal";

LocaleConfig.locales["ko"] = {
  monthNames: [
    "1월",
    "2월",
    "3월",
    "4월",
    "5월",
    "6월",
    "7월",
    "8월",
    "9월",
    "10월",
    "11월",
    "12월",
  ],
  monthNamesShort: [
    "1월",
    "2월",
    "3월",
    "4월",
    "5월",
    "6월",
    "7월",
    "8월",
    "9월",
    "10월",
    "11월",
    "12월",
  ],
  dayNames: [
    "일요일",
    "월요일",
    "화요일",
    "수요일",
    "목요일",
    "금요일",
    "토요일",
  ],
  dayNamesShort: ["일", "월", "화", "수", "목", "금", "토"],
  today: "오늘",
};

LocaleConfig.defaultLocale = "ko";

type CalendarPickerProps = {
  isVisible: boolean;
  setVisible: (visible: boolean) => void;
  onChangeValue: (value: string) => void;
};

const CalendarPickerModal = ({
  isVisible,
  setVisible,
  onChangeValue,
}: CalendarPickerProps) => {
  const [selected, setSelected] = useState("");
  return (
    <Modal
      animationOutTiming={600}
      backdropTransitionOutTiming={600}
      isVisible={isVisible}
      style={{ padding: 0, margin: 0 }}
      onBackdropPress={() => setVisible(false)}
      onBackButtonPress={() => setVisible(false)}
    >
      <View style={styles.modalContent}>
        <View style={styles.titleContainer}>
          <Text style={styles.title}>날짜 선택하기</Text>
        </View>
        <Calendar
          style={{
            marginBottom: 20,
          }}
          onDayPress={day => {
            setSelected(day.dateString);
          }}
          markedDates={{
            [selected]: { selected: true, disableTouchEvent: true },
          }}
          theme={{
            selectedDayBackgroundColor: "#5BADFF",
            todayTextColor: "#5BADFF",
            arrowColor: "#87C5FF",
          }}
        />
        <Button
          onPress={() => {
            setVisible(false);
            onChangeValue(selected);
          }}
        >
          선택 완료
        </Button>
      </View>
    </Modal>
  );
};

const styles = StyleSheet.create({
  modalContent: {
    paddingTop: 5,
    padding: 15,
    paddingBottom: 80,
    width: "100%",
    borderTopRightRadius: 18,
    borderTopLeftRadius: 18,
    backgroundColor: "#fff",
    position: "absolute",
    bottom: 0,
    zIndex: 13,
  },
  titleContainer: {
    height: "16%",
    borderTopRightRadius: 10,
    borderTopLeftRadius: 10,
    paddingHorizontal: 20,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
  },
  title: {
    color: "#3F4245",
    fontSize: 18,
    fontWeight: "bold",
  },
  pickerContainer: {
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center",
    paddingHorizontal: 50,
    paddingVertical: 20,
  },
});

export default CalendarPickerModal;
