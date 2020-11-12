<template>
  <div>
    <b-alert variant="info" show>
      {{ monitor }}
    </b-alert>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

import { BAlert, BButton } from "bootstrap-vue";

@Component({
  components: {
    BAlert,
    BButton,
  },
})
export default class Monitor extends Vue {
  private monitor = "";
  private vm = null;

  mounted() {
    console.log(this.$route.params);
    this.monitor = this.$route.path;
    this.vm = new Vue();
    this.vm.$connect("ws://localhost:9000" + this.monitor, {
      format: "json",
    });
    this.vm.$socket.onopen = () => console.log("web socket openend");
    this.vm.$socket.onerror = (err) => console.log("web socket error : " + err);
    this.vm.$socket.onmessage = (data) =>
      console.log("Received data:", data.data);
  }
}
</script>

<style scoped>
</style>