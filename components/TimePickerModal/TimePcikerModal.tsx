import { StyleSheet, Text, View } from "react-native";
import ReactNativeModal from "react-native-modal";
import Button from "../Button/Button";
import TimePicker from "../TimePicker/TimePicker";
import { useState } from "react";

type TimePickerModalProps = {
  isVisible: boolean;
  setVisible: (visible: boolean) => void;
  onChangeValue: (value: {
    ampm: string;
    hour: string;
    minute: string;
  }) => void;
};

const TimePickerModal = ({
  isVisible,
  setVisible,
  onChangeValue,
}: TimePickerModalProps) => {
  const [time, setTime] = useState<{
    ampm: string;
    hour: string;
    minute: string;
  }>({ ampm: "", hour: "", minute: "" });
  return (
    <ReactNativeModal
      animationOutTiming={600}
      backdropTransitionOutTiming={600}
      isVisible={isVisible}
      style={{ padding: 0, margin: 0 }}
      onBackdropPress={() => setVisible(false)}
      onBackButtonPress={() => setVisible(false)}
    >
      <View style={styles.modalContent}>
        <View style={styles.titleContainer}>
          <Text style={styles.title}>시작 시간</Text>
        </View>
        <View style={{ marginBottom: 20 }}>
          <TimePicker
            itemHeight={50}
            onTimeChange={time => {
              setTime(time);
            }}
          />
        </View>
        <Button
          onPress={() => {
            setVisible(false);
            onChangeValue(time);
          }}
        >
          선택 완료
        </Button>
      </View>
    </ReactNativeModal>
  );
};

const styles = StyleSheet.create({
  modalContent: {
    paddingTop: 10,
    padding: 15,
    paddingBottom: 50,
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

export default TimePickerModal;
