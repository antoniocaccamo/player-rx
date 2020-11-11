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

  mounted() {
    console.log(this.$route.params);
    this.monitor = this.$route.path;
  }
}
</script>

<style scoped>
</style>